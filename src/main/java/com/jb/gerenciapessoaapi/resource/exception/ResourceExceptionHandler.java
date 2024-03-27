package com.jb.gerenciapessoaapi.resource.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jb.gerenciapessoaapi.service.exception.BusinessException;
import com.jb.gerenciapessoaapi.service.exception.RegistroNaoEncontradoException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<StandardError> violacaoRegraNegocio(BusinessException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(HttpStatus.BAD_REQUEST.value());
		err.setError("Tentativa de violação regra de negócio");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(RegistroNaoEncontradoException.class)
	public ResponseEntity<StandardError> registroNaoEncontrado(RegistroNaoEncontradoException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(HttpStatus.NOT_FOUND.value());
		err.setError("Registro não consta no banco de dados");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> argumentoInvalido(MethodArgumentNotValidException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(HttpStatus.BAD_REQUEST.value());
		err.setError(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	
}
