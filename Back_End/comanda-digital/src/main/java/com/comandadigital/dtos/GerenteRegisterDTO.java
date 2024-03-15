package com.comandadigital.dtos;

import org.hibernate.validator.constraints.Length;

import com.comandadigital.models.PerfilModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record GerenteRegisterDTO( @NotBlank(message = "Nome do Gerente precisa ser preenchido")String nome, 
		@NotBlank(message = "Telefone do Gerente precisa ser preenchido") String telefone,
		@NotBlank(message = "CPF do Gerente precisa ser preenchido") String login, 
		 @NotBlank(message = "Senha precisa ser preenchida") @Length(min = 6, max = 12, message = "Senha deve ter entre {min} e {max} caracteres")String senha, 
		 @Valid PerfilModel perfil) {

}
