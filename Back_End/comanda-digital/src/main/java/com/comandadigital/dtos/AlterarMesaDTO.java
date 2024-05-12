package com.comandadigital.dtos;

import org.hibernate.validator.constraints.Length;

import com.comandadigital.dtos.myValidations.DigitsOnly;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlterarMesaDTO(	@NotBlank(message = "cpf precisa ser preenchido") 
								@DigitsOnly(message = "CPF deve conter apenas números")
								@Length(min = 11, max = 11, message = "CPF inválido")
								String cpf,
								@NotNull(message = "Número da mesa atual deve ser preenchido")
								Integer novaMesa,
								@NotNull(message = "Número da nova mesa deve ser preenchido")
								Integer mesaAtual) {

}
