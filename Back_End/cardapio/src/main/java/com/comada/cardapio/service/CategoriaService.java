package com.comada.cardapio.service;

import java.util.List;

import com.comada.cardapio.DTO.request.CategoriaRequestDTO;
import com.comada.cardapio.DTO.response.CategoriaResponseDTO;

public interface CategoriaService {

	CategoriaResponseDTO findById(Long id);
	
	List<CategoriaResponseDTO> findAll(); // retornar  oq ta registrado no sistema
	
	CategoriaResponseDTO register(CategoriaRequestDTO categoriaDTO); // registro
	
	CategoriaResponseDTO update(Long id,CategoriaRequestDTO categoriaDTO); // update
	
	String delete(Long id); // delete
}
