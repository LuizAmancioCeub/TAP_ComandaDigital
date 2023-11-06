package com.comandadigital.dtos;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record StatusRecordDTO(@NotBlank 
		@Length(min = 3, max = 20, message = "O status deverá ter no máximo {max} caracteres")String status) {

}
