package com.jb.gerenciapessoaapi.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jb.gerenciapessoaapi.model.Contato;
import com.jb.gerenciapessoaapi.model.Pessoa;
import com.jb.gerenciapessoaapi.repository.ContatoRepository;
import com.jb.gerenciapessoaapi.repository.PessoaRepository;
import com.jb.gerenciapessoaapi.service.exception.NegocioException;
import com.jb.gerenciapessoaapi.service.exception.RegistroNaoEncontradoException;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ContatoRepository contatoRepository;
	
	@Transactional
	public Pessoa salvar(Pessoa pessoa) {
		validaInformacoesPessoa(pessoa);

		for (Contato contato : pessoa.getContatos()) {
			contato.setPessoa(pessoa);
		}
		pessoa = pessoaRepository.save(pessoa);
		contatoRepository.saveAll(pessoa.getContatos());
		
		return pessoa;
	}
	
	@Transactional(readOnly = true)
	public Pessoa buscarPorId(Long id) {
		Optional<Pessoa> pessoa = Optional.ofNullable(pessoaRepository.findById(id).orElseThrow(
				() -> new RegistroNaoEncontradoException("Pessoa não encontrada.")));
		return pessoa.get();
	}
	
	public Pessoa atualizar(Pessoa pessoa) {
		Pessoa pessoaSalva = buscarPorId(pessoa.getId());
		removerContatos(pessoa, pessoaSalva);
		
		BeanUtils.copyProperties(pessoa, pessoaSalva, "id");
		
		return salvar(pessoaSalva);
	}
	
	@Transactional
	public void excluir(Long id) {
		Optional<Pessoa> pessoa = Optional.ofNullable(pessoaRepository.findById(id).orElseThrow(
				() -> new RegistroNaoEncontradoException("Registro não encotrado para exclusão.")));
		pessoaRepository.deleteById(pessoa.get().getId());
	}
	
	private void validaInformacoesPessoa(Pessoa pessoa) {
		validaDataNascimentoFutura(pessoa);
		validaSePessoaTemContato(pessoa);
	}
	
	private void validaDataNascimentoFutura(Pessoa pessoa) {
		if (pessoa.getDataNascimento().isAfter(LocalDate.now())) {
			throw new NegocioException("Data de nascimento não pode ser maior que data atual");
		}
	}
	
	private void validaSePessoaTemContato(Pessoa pessoa) {
		if (pessoa.getContatos() == null || pessoa.getContatos().isEmpty() || pessoa.getContatos().size() == 0) {
			throw new NegocioException("Pessoa precisa ter pelo menos um contato");
		}
	}
	
	protected void removerContatos(Pessoa pessoaAlterada, Pessoa pessoaSalva) {
		List<Contato> contatosAremover = this.getContatosParaSeremDeletados(pessoaAlterada, pessoaSalva);
		if (contatosAremover.size() > 0 && !contatosAremover.isEmpty()) {
			contatoRepository.deleteAll(contatosAremover);
		}
	}
	
	protected List<Contato> getContatosParaSeremDeletados(Pessoa pessoaAlterada, Pessoa pessoaPersistida) {
		List<Contato> contatosASeremExcluidos = new ArrayList<>();
		
		for (Contato contatoPersistido : pessoaPersistida.getContatos()) {
			if (!pessoaAlterada.getContatos().contains(contatoPersistido)) {
				contatosASeremExcluidos.add(contatoPersistido);
			}
		}
		return contatosASeremExcluidos;
	}
	
}
