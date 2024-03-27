package com.jb.gerenciapessoaapi.repository.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jb.gerenciapessoaapi.filter.PessoaFilter;
import com.jb.gerenciapessoaapi.model.Pessoa;

public interface PessoaRepositoryQuery {
	
	public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable);

}
