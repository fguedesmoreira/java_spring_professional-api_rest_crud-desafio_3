package br.com.flavioguedes.desafio3.controllers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.flavioguedes.desafio3.dto.errors.CustomErrorDTO;
import br.com.flavioguedes.desafio3.dto.errors.ValidationErrorDTO;
import br.com.flavioguedes.desafio3.exceptions.DataBaseException;
import br.com.flavioguedes.desafio3.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerController {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<CustomErrorDTO> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;

		CustomErrorDTO err = new CustomErrorDTO(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);

	}

	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<CustomErrorDTO> resourceNotFound(DataBaseException e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		CustomErrorDTO err = new CustomErrorDTO(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CustomErrorDTO> methodArgumentNotValidation(MethodArgumentNotValidException e,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

		ValidationErrorDTO err = new ValidationErrorDTO(Instant.now(), status.value(), "Dados inválidos.",
				request.getRequestURI());

		for (FieldError f : e.getBindingResult().getFieldErrors()) {
			err.addError(f.getField(), f.getDefaultMessage());
		}

		return ResponseEntity.status(status).body(err);

	}

	
}