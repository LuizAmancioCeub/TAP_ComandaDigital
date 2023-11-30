package com.comandadigital.dtos;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.comandadigital.models.CategoriaModel;
import com.comandadigital.models.StatusModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemRecordDTO(@NotBlank(message = "Nome deve ser preenchido") @Length(min = 3, max = 25, message = "O nome deverá ter entre {min} e {max} caracteres" ) String nome, 
							@NotBlank @Length(min = 10, max = 105, message = "A descrição deverá ter entre {min} e {max} caracteres" ) String descricao, 
							@NotNull(message = "O preço não pode ser nulo") double preco, 
							@NotBlank @Length(min = 3, max = 45, message = "O nome da imagem deverá ter entre {min} e {max} caracteres" ) String imagem, 
							@Valid CategoriaModel categoria, 
							@Valid StatusModel status) {

}
