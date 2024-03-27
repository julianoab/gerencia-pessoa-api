package com.jb.gerenciapessoaapi.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jb.gerenciapessoaapi.DTO.PessoaDTO;
import com.jb.gerenciapessoaapi.mapper.PessoaMapper;
import com.jb.gerenciapessoaapi.model.Contato;
import com.jb.gerenciapessoaapi.model.Pessoa;
import com.jb.gerenciapessoaapi.repository.ContatoRepository;
import com.jb.gerenciapessoaapi.repository.PessoaRepository;
import com.jb.gerenciapessoaapi.service.exception.BusinessException;
import com.jb.gerenciapessoaapi.service.exception.RegistroNaoEncontradoException;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaMapper pessoaMapper;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ContatoRepository contatoRepository;
	
	@Transactional
	public PessoaDTO salvar(PessoaDTO pessoaDTO) {
		Pessoa pessoa = pessoaMapper.converteDtoParaEntidade(pessoaDTO);
		validaInformacoesPessoa(pessoa);

		for (Contato contato : pessoa.getContatos()) {
			contato.setPessoa(pessoa);
		}
		pessoa = pessoaRepository.save(pessoa);
		contatoRepository.saveAll(pessoa.getContatos());
		
		return pessoaMapper.converteEntidadeParaDto(pessoa);
	}
	
	public PessoaDTO buscarPorId(Long id) {
		Optional<Pessoa> pessoa = Optional.ofNullable(pessoaRepository.findById(id).orElseThrow(() -> new RegistroNaoEncontradoException("Pessoa não encontrada")));
		return pessoaMapper.converteEntidadeParaDto(pessoa.get());
	}
	
	public PessoaDTO atualizar(PessoaDTO pessoa) {
		Pessoa pessoaSalva = pessoaMapper.converteDtoParaEntidade(buscarPorId(pessoa.getId()));
		removerContatos(pessoaMapper.converteDtoParaEntidade(pessoa), pessoaSalva);
		
		BeanUtils.copyProperties(pessoa, pessoaSalva, "id");
		
		return salvar(pessoaMapper.converteEntidadeParaDto(pessoaSalva));
	}
	
	public void excluir(Long id) {
		pessoaRepository.deleteById(id);
	}
	
	private void validaInformacoesPessoa(Pessoa pessoa) {
		validaDataNascimentoFutura(pessoa);
		validaSePessoaTemContato(pessoa);
	}
	
	private void validaDataNascimentoFutura(Pessoa pessoa) {
		if (pessoa.getDataNascimento().isAfter(LocalDate.now())) {
			throw new BusinessException("Data de nascimento não pode ser maior que data atual");
		}
	}
	
	private void validaSePessoaTemContato(Pessoa pessoa) {
		if (pessoa.getContatos() == null || pessoa.getContatos().isEmpty() || pessoa.getContatos().size() == 0) {
			throw new BusinessException("Pessoa precisa ter pelo menos um contato");
		}
	}
	
	private void removerContatos(Pessoa pessoaAlterada, Pessoa pessoaSalva) {
		List<Contato> contatosAremover = this.getContatosParaSeremDeletados(pessoaAlterada, pessoaSalva);
		if (contatosAremover.size() > 0 && !contatosAremover.isEmpty()) {
			contatoRepository.deleteAll(contatosAremover);
		}
	}
	
	private List<Contato> getContatosParaSeremDeletados(Pessoa pessoaAlterada, Pessoa pessoaPersistida) {
		List<Contato> contatosASeremExcluidos = new ArrayList<>();
		
		for (Contato contatoPersistido : pessoaPersistida.getContatos()) {
			if (!pessoaAlterada.getContatos().contains(contatoPersistido)) {
				contatosASeremExcluidos.add(contatoPersistido);
			}
		}
		return contatosASeremExcluidos;
	}
	
}
