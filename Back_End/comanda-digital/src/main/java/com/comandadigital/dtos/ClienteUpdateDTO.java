package com.comandadigital.dtos;

import org.hibernate.validator.constraints.Length;

import com.comandadigital.dtos.myValidations.DigitsOnly;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteUpdateDTO(	@NotBlank(message = "cpf precisa ser preenchido") 
								@DigitsOnly(message = "CPF deve conter apenas números")
								@Length(min = 11, max = 11, message = "CPF inválido") String cpf, 
								
								@NotBlank(message = "Nome precisa ser preenchido") 
								@Length(min = 3, max = 20, message = "Nome deve ter entre {min} e {max} caracteres") String nome, 
								
								@NotBlank(message = "Número de telefone precisa ser preenchido")
								@DigitsOnly(message = "Número de telefone deve conter apenas números")
								@Length(min = 11, max = 11, message = "Número de telefone inválido") String telefone, 
								
								@NotBlank(message = "Email precisa ser preenchido")
								@Email(message = "Email inválido") String email, 
								
								@NotNull(message = "Perfil não encontrado") Integer perfil) {

}
