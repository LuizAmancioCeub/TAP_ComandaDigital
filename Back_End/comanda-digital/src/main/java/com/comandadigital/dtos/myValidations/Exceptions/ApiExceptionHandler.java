package com.comandadigital.dtos.myValidations.Exceptions;

import java.net.URI;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestControllerAdvice // vai ser um capturador de exceção global
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	private final MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		ProblemDetail problemDetail = ProblemDetail.forStatus(status);
		problemDetail.setDetail("Um ou mais campos estão inválidos");
		problemDetail.setType(URI.create("https://comanda-digital.com/erros/campos-invalidos"));
		
		// pegar lista de erros, navegando nela e coletando ele em um map com chave e valor
		var fields = ex.getBindingResult().getAllErrors().stream()
				.collect(Collectors.toMap(error -> ((FieldError) error).getField(), error -> messageSource.getMessage(error, LocaleContextHolder.getLocale())));
		
		problemDetail.setProperty("fields",fields );
		
		return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
	}
	
	// captura as exceções
		@ExceptionHandler(NegocioException.class)
		public ProblemDetail handleNegocio(NegocioException e) {
			ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
			problemDetail.setDetail(e.getMessage());
			problemDetail.setType(URI.create("https://comanda-digital.com/erros/regra-negocio"));
			return problemDetail;
		}
		
		
		@ExceptionHandler(Exception.class)
		public ProblemDetail handleException(Exception e) {
			ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
			problemDetail.setDetail(e.getMessage());
			problemDetail.setType(URI.create("https://comanda-digital.com/erros/erro-inesperado"));
			return problemDetail;
		}
		
		@ExceptionHandler
		public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException e) {
			ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
			problemDetail.setDetail("Recurso esta em uso");
			problemDetail.setType(URI.create("https://comanda-digital.com/erros/recurso-em-uso"));
			return problemDetail;
		}
}
