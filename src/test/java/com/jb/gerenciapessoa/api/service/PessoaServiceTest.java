package com.jb.gerenciapessoa.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jb.gerenciapessoaapi.model.Contato;
import com.jb.gerenciapessoaapi.model.Pessoa;
import com.jb.gerenciapessoaapi.repository.ContatoRepository;
import com.jb.gerenciapessoaapi.repository.PessoaRepository;
import com.jb.gerenciapessoaapi.service.PessoaService;
import com.jb.gerenciapessoaapi.service.exception.NegocioException;
import com.jb.gerenciapessoaapi.service.exception.RegistroNaoEncontradoException;

public class PessoaServiceTest {

	@InjectMocks
	private PessoaService pessoaService;

	@Mock
	private PessoaRepository pessoaRepository;

	@Mock
	private ContatoRepository contatoRepository;

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	public void afterEach() {
		Mockito.clearAllCaches();
	}

	@Test
	public void deveSalvarPessoa() {
		when(pessoaRepository.save(getPessoa())).thenReturn(getPessoa());

		pessoaService.salvar(getPessoa());

		verify(pessoaRepository, times(1)).save(getPessoa());
		verify(contatoRepository, times(1)).saveAll(getPessoa().getContatos());
	}

	@Test
	public void devePessoaBuscaPorId() {
		when(pessoaRepository.findById(1L)).thenReturn(Optional.ofNullable(getPessoa()));

		Pessoa pessoa = pessoaService.buscarPorId(1L);

		assertEquals("Marcos", pessoa.getNome());
	}

	@Test
	public void deveAtualizarPessoa() {
		Pessoa pessoaMock = new Pessoa();
		pessoaMock.setNome("Marcos");
		pessoaMock.setCpf("9999999999");
		pessoaMock.setDataNascimento(LocalDate.now());
		List<Contato> contatos = new ArrayList<Contato>();
		Contato contatoMock = new Contato();
		contatoMock.setEmail("email@email.com");
		contatoMock.setNome("Maria");
		contatoMock.setTelefone("3333-3333");
		Contato contatoMock1 = new Contato();
		contatoMock1.setEmail("email@email.com");
		contatoMock1.setNome("Maria");
		contatoMock1.setTelefone("3333-3333");
		contatos.add(contatoMock);
		contatos.add(contatoMock1);
		pessoaMock.setContatos(contatos);
		when(pessoaRepository.findById(1L)).thenReturn(Optional.ofNullable(getPessoa()));
		when(pessoaRepository.save(getPessoa())).thenReturn(getPessoa());

		pessoaService.salvar(pessoaMock);

		verify(pessoaRepository, times(1)).save(getPessoa());
		verify(contatoRepository, times(1)).saveAll(getPessoa().getContatos());
	}

	@Test
	public void deveExcluirPessoa() {
		Pessoa pessoa = new Pessoa();
		pessoa.setId(1L);
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.ofNullable(pessoa));
		pessoaService.excluir(pessoa.getId());

		verify(pessoaRepository, times(1)).deleteById(pessoa.getId());
	}

	@Test
	public void deveLancarExcecaoSePessoaNaoPossuiContato() {
		Pessoa pessoaMock = new Pessoa();
		pessoaMock.setNome("Marcos");
		pessoaMock.setCpf("9999999999");
		pessoaMock.setDataNascimento(LocalDate.now());
		pessoaMock.setContatos(Collections.emptyList());

		NegocioException exception = assertThrows(NegocioException.class, () -> pessoaService.salvar(pessoaMock));

		assertTrue(exception.getMessage().contains("Pessoa precisa ter pelo menos um contato"));
	}

	@Test
	public void deveLancarExcecaoDataNascimentoMaiorDataAtual() {
		Pessoa pessoaMock = new Pessoa();
		pessoaMock.setNome("Marcos");
		pessoaMock.setCpf("9999999999");
		pessoaMock.setDataNascimento(LocalDate.now().plusDays(3));
		pessoaMock.setContatos(Collections.emptyList());

		NegocioException exception = assertThrows(NegocioException.class,() -> pessoaService.salvar(pessoaMock));

		assertTrue(exception.getMessage().contains("Data de nascimento não pode ser maior que data atual"));
	}

	@Test
	public void deveLancarExcecaoPessoaNaBuscaPorId() {
		when(pessoaRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

		RegistroNaoEncontradoException exception = assertThrows(RegistroNaoEncontradoException.class, () -> pessoaService.buscarPorId(1L));

		assertTrue(exception.getMessage().contains("Pessoa não encontrada."));
	}

	private Pessoa getPessoa() {
		Pessoa pessoaMock = new Pessoa();
		pessoaMock.setNome("Marcos");
		pessoaMock.setCpf("9999999999");
		pessoaMock.setDataNascimento(LocalDate.now());

		List<Contato> contatos = new ArrayList<Contato>();
		Contato contatoMock = new Contato();
		contatoMock.setEmail("email@email.com");
		contatoMock.setNome("Maria");
		contatoMock.setTelefone("3333-3333");
		contatos.add(contatoMock);

		pessoaMock.setContatos(contatos);

		return pessoaMock;
	}

}
