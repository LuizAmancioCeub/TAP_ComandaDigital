package com.comada.cardapio.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.comada.cardapio.DTO.request.CategoriaRequestDTO;
import com.comada.cardapio.DTO.response.CategoriaResponseDTO;
import com.comada.cardapio.entity.Categoria;

// Classe para converter DTO em uma Categoria
@Component
public class CategoriaMapper {
	public Categoria toCategoria(CategoriaRequestDTO categoriaDTO) {
		// builder é do lombock -> Categoria categoria = new categoria()
		return Categoria.builder()
				.categoria(categoriaDTO.getCategoria())
				.build();
		// DTO -> entidade
	}
	
	public CategoriaResponseDTO toCategoriaDTO(Categoria categoria) {
		return new CategoriaResponseDTO(categoria);
		
		// Entidade -> DTO
	}
	
	
	public List<CategoriaResponseDTO> toCategoriaListDTO(List<Categoria> categoriaList){
		return categoriaList.stream().map(CategoriaResponseDTO::new).collect(Collectors.toList()); // convertendo lista de Categoria entidade para Lista DTO
	}
	
	public void updateCategoriaData(Categoria categoria, CategoriaRequestDTO categoriaDTO ) {
		
		categoria.setCategoria(categoriaDTO.getCategoria());
	}
	
}
