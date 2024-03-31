package com.comandadigital.dtos.myValidations.Exceptions;

public class NegocioException extends RuntimeException {
	
	public NegocioException(String mensagem) {
		super(mensagem);
	}
}
