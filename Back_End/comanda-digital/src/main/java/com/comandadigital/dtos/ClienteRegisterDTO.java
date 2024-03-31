package com.comandadigital.dtos;

import org.hibernate.validator.constraints.Length;

import com.comandadigital.dtos.myValidations.DigitsOnly;
import com.comandadigital.models.PerfilModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record ClienteRegisterDTO(@NotBlank(message = "cpf precisa ser preenchido") 
								 @DigitsOnly(message = "CPF deve conter apenas números")
								 @Length(min = 11, max = 11, message = "CPF inválido") String cpf, 
								 
							     @NotBlank(message = "Nome precisa ser preenchido") 
								 @Length(min = 3, max = 20, message = "Nome deve ter entre {min} e {max} caracteres") String nome, 
								 
							     @NotBlank(message = "Senha precisa ser preenchida") 
								 @Length(min = 6, max = 12, message = "Senha deve ter entre {min} e {max} caracteres") String senha, 
							     
								 @NotBlank(message = "Número de telefone precisa ser preenchido")
								 @DigitsOnly(message = "Número de telefone deve conter apenas números")
								 @Length(min = 11, max = 11, message = "Número de telefone inválido") String telefone, 
								 
								 @Valid PerfilModel perfil
							   ) {

}
