package com.comandadigital.dtos;

import org.hibernate.validator.constraints.Length;

import com.comandadigital.dtos.myValidations.CategoriaUnique;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRecordDTO(@NotBlank(message = "Informe o nome da Categoria") 
								@Length(min = 3, max = 20, message = "O nome deverá ter entre {min} e {max} caracteres" )
								@CategoriaUnique 
								String categoria) { 
	
}
