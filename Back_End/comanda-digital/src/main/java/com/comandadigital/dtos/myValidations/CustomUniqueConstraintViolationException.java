package com.comandadigital.dtos.myValidations;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// classe para customizar erro 500
public class CustomUniqueConstraintViolationException extends ResponseStatusException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4200760202157571751L;

	public CustomUniqueConstraintViolationException(String mensagem) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, mensagem);
	}
}
