package com.comandadigital.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.ItemRecordDTO;
import com.comandadigital.dtos.ItemUpdateRecordDTO;
import com.comandadigital.models.CategoriaModel;
import com.comandadigital.models.ItemModel;
import com.comandadigital.services.CategoriaServiceImplements;
import com.comandadigital.services.ItemServiceImplements;

import jakarta.validation.Valid;

@RestController
public class ItemController {
	@Autowired
	ItemServiceImplements itemServiceImplements;
	@Autowired
	CategoriaServiceImplements categoriaServiceImplements;
	
	// Registrar Item
		@PostMapping("/item")
		public ResponseEntity<ItemModel> saveCategoria(@RequestBody @Valid ItemRecordDTO itemDto){

	        return ResponseEntity.status(HttpStatus.CREATED).body(itemServiceImplements.register(itemDto));
		}
		
		
		// lista dos itens
		@GetMapping("/itens")
		public ResponseEntity<List<ItemModel>> getAllItens(){
					
			// utilizando hateoas para referenciar o produto na lista
			List<ItemModel> itemList = itemServiceImplements.findAll();
					
			return ResponseEntity.status(HttpStatus.OK).body(itemList);
		}
		
		// Buscar Item por id
		@GetMapping("/item/{id}")
		public ResponseEntity<Object> getOneItemId(@PathVariable(value="id") Integer id){
			Optional<ItemModel> item0 = itemServiceImplements.findById(id);
			
			if(item0 == null) {
				 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não encontrado!");
			}
			return ResponseEntity.status(HttpStatus.OK).body(item0.get());
		}
		
		@PutMapping("/item/{id}")
		public ResponseEntity<Object> updateItem(@PathVariable(value="id") Integer id, @RequestBody @Valid ItemUpdateRecordDTO dto){
			
			ItemModel updateItem = itemServiceImplements.update(id, dto);
			
			 if (updateItem == null) {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível alterar o item!!");
	         }
			
	         return ResponseEntity.status(HttpStatus.OK).body(updateItem);
		}
		
		// deletar pelo id
	 	@DeleteMapping("/item/{id}")
	 	public ResponseEntity<Object> deleteItem(@PathVariable(value="id")Integer id){
	 		
	 		return ResponseEntity.status(HttpStatus.OK).body(itemServiceImplements.delete(id));
	 	}
				
				
				
				
	// Consultar Item por categoria
		@GetMapping("/categoria/{categoriaId}/itens")
		public ResponseEntity<List<ItemModel>> getItensByCategoria(@PathVariable("categoriaId") Integer categoriaId) {
			
			Optional<CategoriaModel> categoria = categoriaServiceImplements.findById(categoriaId);
			
			if (categoria.isEmpty()) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		    }
			
			List<ItemModel> itens = itemServiceImplements.findItemByCategoria(categoria.get());
			

		    return ResponseEntity.status(HttpStatus.OK).body(itens);
		}
		
}
