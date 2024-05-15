package com.comandadigital.dtos;

import org.hibernate.validator.constraints.Length;

import com.comandadigital.dtos.myValidations.DigitsOnly;
import com.comandadigital.models.PerfilModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GarcomRegisterDTO(@NotBlank(message = "Nome do Garçom precisa ser preenchido")
								@Length(min = 3, max = 20,message = "Nome deve ter entre {min} e {max} caracteres")
								String nome, 
								
								@NotBlank(message = "Telefone do Garçom precisa ser preenchido") 
								@DigitsOnly(message = "CPF deve conter apenas números") 
								@Length(min = 11, max = 11, message = "Telefone inválido")
								String telefone,
								
								@NotBlank(message = "Email precisa ser preenchido")
								@Email(message = "Email inválido") String email,
								
								@NotBlank(message = "CPF do Garçom precisa ser preenchido") 
								@DigitsOnly(message = "CPF deve conter apenas números") 
								@Length(min = 11, max = 11, message = "CPF inválido")
								String cpf, 
								
								@NotBlank(message = "Senha precisa ser preenchida") @Length(min = 6, max = 12, message = "Senha deve ter entre {min} e {max} caracteres")
								String senha, 
								
								@Valid PerfilModel perfil) {

}
