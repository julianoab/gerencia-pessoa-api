package com.jb.gerenciapessoaapi.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;

import com.jb.gerenciapessoaapi.DTO.ContatoDTO;
import com.jb.gerenciapessoaapi.model.Contato;

@Configuration
public class ContatoMapper {
	
	public Contato converteDTOEntidade(ContatoDTO contatoDTO) {
		Contato contato = new Contato();
		contato.setId(contatoDTO.getId());
		contato.setNome(contatoDTO.getNome());
		contato.setTelefone(contatoDTO.getTelefone());
		contato.setEmail(contatoDTO.getEmail());
		
		return contato;
	}
	
	public ContatoDTO converteEntidadeDTO(Contato contato) {
		ContatoDTO contatoDTO = new ContatoDTO();
		contatoDTO.setId(contato.getId());
		contatoDTO.setNome(contato.getNome());
		contatoDTO.setTelefone(contato.getTelefone());
		contatoDTO.setEmail(contato.getEmail());
		
		return contatoDTO;
	}
	
	public List<ContatoDTO> converteListaEntidadeParaDTO(List<Contato> contatos) {
		return contatos.stream().map(this::converteEntidadeDTO).collect(Collectors.toList());
	}
	
	public List<Contato> converteListaDTOParaEntidade(List<ContatoDTO> contatos) {
		return contatos.stream().map(this::converteDTOEntidade).collect(Collectors.toList());
	}

}
