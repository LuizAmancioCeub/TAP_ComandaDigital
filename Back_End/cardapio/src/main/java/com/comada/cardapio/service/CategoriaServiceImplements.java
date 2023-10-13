package com.comada.cardapio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.comada.cardapio.DTO.request.CategoriaRequestDTO;
import com.comada.cardapio.DTO.response.CategoriaResponseDTO;
import com.comada.cardapio.entity.Categoria;
import com.comada.cardapio.repository.CategoriaRepository;
import com.comada.cardapio.util.CategoriaMapper;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class CategoriaServiceImplements implements CategoriaService {
	
	private final CategoriaRepository categoriaRepository;
	private final CategoriaMapper categoriaMapper;

	@Override
	public CategoriaResponseDTO findById(Long id) {
		
		return categoriaMapper.toCategoriaDTO(returnCategoria(id)); // convertendo entidade em DTO
	}

	@Override
	public List<CategoriaResponseDTO> findAll() {
		
		return categoriaMapper.toCategoriaListDTO(categoriaRepository.findAll());
	}

	@Override
	public CategoriaResponseDTO register(CategoriaRequestDTO categoriaDTO) {
		Categoria categoria = categoriaMapper.toCategoria(categoriaDTO);
		
		return categoriaMapper.toCategoriaDTO(categoriaRepository.save(categoria));
	}

	@Override
	public CategoriaResponseDTO update(Long id,CategoriaRequestDTO categoriaDTO) {
		
		Categoria categoria = returnCategoria(id); // recuperado id da categoria
		
		categoriaMapper.updateCategoriaData(categoria, categoriaDTO); // atualizando dados da categoria
		
		return categoriaMapper.toCategoriaDTO(categoriaRepository.save(categoria)); // salvando e retornando
	}

	@Override
	public String delete(Long id) {
		categoriaRepository.deleteById(id);
		return "Categoria "+id+" deletada com sucesso";
	}
	
	private Categoria returnCategoria(Long id) { // metodo para verificar id
		return categoriaRepository.findById(id).orElseThrow(()-> new RuntimeException("Categoria não encontrada na base de dados"));
	}

}
