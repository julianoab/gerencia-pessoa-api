package com.jb.gerenciapessoaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jb.gerenciapessoaapi.model.Contato;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

}
