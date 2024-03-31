package com.comandadigital.dtos;

import com.comandadigital.models.CategoriaModel;
import com.comandadigital.models.StatusModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemUpdateRecordDTO(
		@NotBlank(message = "Item deve conter nome") 
		@Size(min = 3, max = 25, message = "Nome do item deverá ter entre {min} e {max} caracteres") 
		String nome,
		@NotBlank(message = "Item deve conter descrição")
		@Size(min = 5, max = 105, message = "Descrição deverá ter entre {min} e {max} caracteres")
		 String descricao,
		 @NotNull(message = "Item deve conter preço")
		 double preco, 
		 @NotBlank(message = "Item deve conter imagem")
		 String imagem,
		 @Valid
		CategoriaModel categoria, 
		@Valid
		 StatusModel status) {

}
