package com.jb.gerenciapessoaapi.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.jb.gerenciapessoaapi.DTO.PessoaDTO;
import com.jb.gerenciapessoaapi.model.Pessoa;

@Configuration
public class PessoaMapper {
	
	@Autowired
	private ContatoMapper contatoMapper;
	
	public Pessoa converteDtoParaEntidade(PessoaDTO pessoaDTO) {
		Pessoa pessoa = new Pessoa();
		pessoa.setId(pessoaDTO.getId());
		pessoa.setNome(pessoaDTO.getNome());
		pessoa.setCpf(pessoaDTO.getCpf());
		pessoa.setDataNascimento(pessoaDTO.getDataNascimento());
		pessoa.setContatos(contatoMapper.converteListaDTOParaEntidade(pessoaDTO.getContatos()));

		return pessoa;
	}

	public PessoaDTO converteEntidadeParaDto(Pessoa pessoa) {
		PessoaDTO pessoaDTO = new PessoaDTO();
		pessoaDTO.setId(pessoa.getId());
		pessoaDTO.setNome(pessoa.getNome());
		pessoaDTO.setCpf(pessoa.getCpf());
		pessoaDTO.setDataNascimento(pessoa.getDataNascimento());
		pessoaDTO.setContatos(contatoMapper.converteListaEntidadeParaDTO(pessoa.getContatos()));
		
		return pessoaDTO;
	}
	
	public List<PessoaDTO> converteListaEntidadeParaDTO(List<Pessoa> pessoas) {
		return pessoas.stream().map(this::converteEntidadeParaDto).collect(Collectors.toList());
	}
	
	public List<Pessoa> converteListaDTOParaEntidade(List<PessoaDTO> pessoasDTO) {
		return pessoasDTO.stream().map(this::converteDtoParaEntidade).collect(Collectors.toList());
	}
	
}
