package com.comandadigital.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import com.comandadigital.dtos.CategoriaRecordDTO;
import com.comandadigital.models.CategoriaModel;
import com.comandadigital.services.CategoriaServiceImplements;

import jakarta.validation.Valid;

@RestController
public class CategoriaController {

	@Autowired
	CategoriaServiceImplements categoriaServiceImplements;
	
	
	// Registrar categoria
	@PostMapping("/categorias")
	public ResponseEntity<CategoriaModel> saveCategoria(@RequestBody @Valid CategoriaRecordDTO categoriaDto){

        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaServiceImplements.register(categoriaDto));
	}
	
	
	// lista das categorias
		@GetMapping("/categorias")
		public ResponseEntity<List<CategoriaModel>> getAllCategorias(){
			
			// utilizando hateoas para referenciar o produto na lista
			List<CategoriaModel> CategoriaList = categoriaServiceImplements.findAll();
			
			if(!CategoriaList.isEmpty()) {
				
				for(CategoriaModel Categoria : CategoriaList) {
					Integer id = Categoria.getId();
					
					// Adicione um link para acessar as informações detalhadas do produto (self)
					Categoria.add(linkTo(methodOn(CategoriaController.class).getOneCategoriaId(id)).withSelfRel());
					
					// Adicione um link para a operação de edição do produto
					Categoria.add(linkTo(methodOn(CategoriaController.class).updateCategoria(id,null)).withRel("editar"));
					
					// Adicione um link para a operação de exclusão do produto
					Categoria.add(linkTo(methodOn(CategoriaController.class).deleteCategoria(id)).withRel("deletar"));
				}
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(CategoriaList);
		}
	
	
	 // Método para buscar categoria por ID
    @GetMapping("/categorias/{id}")
    public ResponseEntity<Object> getOneCategoriaId(@PathVariable(value = "id") Integer id) {
        Optional<CategoriaModel> categoria0 = categoriaServiceImplements.findById(id);

        if (categoria0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada!");
        }
        
     // Adicione um link para visualizar itens da categoria
        CategoriaModel categoria = categoria0.get();
        categoria.add(linkTo(methodOn(ItemController.class).getItensByCategoria(id)).withRel("Itens_Categoria"));

        return ResponseEntity.status(HttpStatus.OK).body(categoria0.get());
    }
    
 // atualizar pelo id
 	@PutMapping("/categorias/{id}")
 	public ResponseEntity<Object> updateCategoria(@PathVariable(value="id")Integer id,@RequestBody @Valid CategoriaRecordDTO dto){
 		 
 		CategoriaModel updatedCategoria = categoriaServiceImplements.update(id, dto);

         if (updatedCategoria == null) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada!");
         }

         return ResponseEntity.status(HttpStatus.OK).body(updatedCategoria);
        
 	}
 	
 	// deletar pelo id
 	@DeleteMapping("/categorias/{id}")
 	public ResponseEntity<Object> deleteCategoria(@PathVariable(value="id")Integer id){
 		
 		return ResponseEntity.status(HttpStatus.OK).body(categoriaServiceImplements.delete(id));
 	}
}
