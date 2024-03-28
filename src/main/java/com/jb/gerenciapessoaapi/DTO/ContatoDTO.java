package com.jb.gerenciapessoaapi.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class ContatoDTO {
	
	private Long id;
	
	@NotNull(message = "Nome do contato é obrigatório")
	private String nome;
	
	@NotNull(message = "Telefone do contato é obrigatório")
	private String telefone;
	
	@Email
	@NotNull(message = "Email do contato é obrigatório")
	private String email;
	
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
	
	public String getTelefone() {
		return telefone;
	}
	
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

}
