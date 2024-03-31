package com.comandadigital.services;

import java.util.List;
import java.util.Optional;

import com.comandadigital.dtos.ItemRecordDTO;
import com.comandadigital.dtos.ItemUpdateRecordDTO;
import com.comandadigital.models.CategoriaModel;
import com.comandadigital.models.ItemModel;

public interface ItemService {
	
	boolean existsByNome(String novoNome, String nomeAtual);
	
	List<ItemModel> findItemByCategoria(CategoriaModel categoria);
	
	Optional<ItemModel> findById(Integer id);
	
	List<ItemModel> findAll(); // retornar  oq ta registrado no sistema
	
	ItemModel register( ItemRecordDTO itemDTO); // registro
	
	ItemModel update(Integer id, ItemUpdateRecordDTO itemDTO); // update
	
	String delete(Integer id); // delete
}
