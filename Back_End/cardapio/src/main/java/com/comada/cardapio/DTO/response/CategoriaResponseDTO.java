package com.comada.cardapio.DTO.response;

import com.comada.cardapio.entity.Categoria;

import lombok.Getter;

// classe para receber a resposta
@Getter
public class CategoriaResponseDTO {
	
	private Long id;
	
	private String categoria;
	
	public CategoriaResponseDTO(Categoria categoria) {
		this.id = categoria.getId();
		this.categoria = categoria.getCategoria();
	}
	
}
