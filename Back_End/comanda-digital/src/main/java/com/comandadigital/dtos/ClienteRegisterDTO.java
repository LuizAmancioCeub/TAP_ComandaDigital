package com.comandadigital.dtos;

import org.hibernate.validator.constraints.Length;

import com.comandadigital.models.PerfilModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record ClienteRegisterDTO(@NotBlank(message = "cpf precisa ser preenchido") @Length(min = 11, max = 11, message = "cpf inválido") String cpf, 
							   @NotBlank(message = "Nome precisa ser preenchido") @Length(min = 3, max = 20, message = "Nome deve ter entre {min} e {max} caracteres") String nome, 
							   @NotBlank(message = "Senha precisa ser preenchida") @Length(min = 6, max = 12, message = "Senha deve ter entre {min} e {max} caracteres") String senha, 
							   @NotBlank(message = "Telefone precisa ser preenchido") @Length(min = 11, max = 11, message = "número de telefone inválido") String telefone, 
							   @Valid PerfilModel perfil
							   ) {

}
