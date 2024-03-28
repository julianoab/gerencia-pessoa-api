package com.jb.gerenciapessoaapi.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jb.gerenciapessoaapi.DTO.PessoaDTO;
import com.jb.gerenciapessoaapi.filter.PessoaFilter;
import com.jb.gerenciapessoaapi.mapper.PessoaMapper;
import com.jb.gerenciapessoaapi.model.Pessoa;
import com.jb.gerenciapessoaapi.repository.PessoaRepository;
import com.jb.gerenciapessoaapi.service.PessoaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private PessoaMapper pessoaMapper;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@GetMapping("/listar-todas")
	public List<Pessoa> listar() {
		return pessoaRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PessoaDTO> buscarPeloId(@PathVariable("id") Long id) {
		 PessoaDTO pessoa = pessoaMapper.converteEntidadeParaDto(pessoaService.buscarPorId(id));
		return ResponseEntity.ok(pessoa);
	}
	
	@PostMapping
	public ResponseEntity<PessoaDTO> criar(@Valid @RequestBody PessoaDTO pessoaDTO) {
		Pessoa pessoa = pessoaMapper.converteDtoParaEntidade(pessoaDTO);
		PessoaDTO pessoaSalva = pessoaMapper.converteEntidadeParaDto(pessoaService.salvar(pessoa)) ;
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	@PutMapping
	public ResponseEntity<PessoaDTO> atualizar(@Valid @RequestBody PessoaDTO pessoaDTO) {
		Pessoa pessoa = pessoaMapper.converteDtoParaEntidade(pessoaDTO);
		PessoaDTO pessoaSalva = pessoaMapper.converteEntidadeParaDto(pessoaService.atualizar(pessoa)) ;
		return ResponseEntity.ok(pessoaSalva);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable("id") Long id) {
		pessoaService.excluir(id);
	}
	
	@GetMapping
	public Page<Pessoa> pesquisar(PessoaFilter pessoaFilter, Pageable pageable) {
		return pessoaRepository.filtrar(pessoaFilter, pageable);
	}
	
}
