package com.jb.gerenciapessoaapi.filter;

import java.time.LocalDate;

public class PessoaFilter {
	
	private String nome;
	
	private LocalDate dataNascimento;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
}
