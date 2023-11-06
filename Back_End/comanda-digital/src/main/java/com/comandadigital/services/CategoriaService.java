package com.comandadigital.services;

import java.util.List;
import java.util.Optional;

import com.comandadigital.dtos.CategoriaRecordDTO;
import com.comandadigital.dtos.myValidations.CategoriaUnique;
import com.comandadigital.models.CategoriaModel;

public interface CategoriaService {
	
	boolean existsByCategoria(String categoria);
	
	Optional<CategoriaModel> findById(Integer id);
	
	List<CategoriaModel> findAll(); // retornar  oq ta registrado no sistema
	
	CategoriaModel register(CategoriaRecordDTO categoriaDTO); // registro
	
	CategoriaModel update(Integer id,@CategoriaUnique CategoriaRecordDTO categoriaDTO); // update
	
	String delete(Integer id); // delete
	
}
