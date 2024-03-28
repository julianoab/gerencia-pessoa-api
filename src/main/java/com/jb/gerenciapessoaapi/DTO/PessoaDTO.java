package com.jb.gerenciapessoaapi.DTO;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotNull;

public class PessoaDTO {
	
	private Long id;
	
	@NotNull(message = "Nome é obrigatóario")
	private String nome;
	
	@CPF(message = "Cpf inválido")
	private String cpf;
	
	@NotNull(message = "Data nascimento é obrigatória")
	private LocalDate dataNascimento;
	
	@NotNull(message = "Necessário no minimo um contato")
	private List<ContatoDTO> contatos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public List<ContatoDTO> getContatos() {
		return contatos;
	}

	public void setContatos(List<ContatoDTO> contatos) {
		this.contatos = contatos;
	}
	
}
