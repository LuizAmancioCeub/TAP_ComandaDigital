package com.comandadigital.services;

import java.util.List;
import java.util.Optional;

import com.comandadigital.dtos.ItemRecordDTO;
import com.comandadigital.dtos.myValidations.ItemUnique;
import com.comandadigital.models.CategoriaModel;
import com.comandadigital.models.ItemModel;

public interface ItemService {
	
	boolean existsByNome(String nome);
	
	List<ItemModel> findItemByCategoria(CategoriaModel categoria);
	
	Optional<ItemModel> findById(Integer id);
	
	List<ItemModel> findAll(); // retornar  oq ta registrado no sistema
	
	ItemModel register(ItemRecordDTO itemDTO); // registro
	
	ItemModel update(Integer id,@ItemUnique ItemRecordDTO itemDTO); // update
	
	String delete(Integer id); // delete
}
