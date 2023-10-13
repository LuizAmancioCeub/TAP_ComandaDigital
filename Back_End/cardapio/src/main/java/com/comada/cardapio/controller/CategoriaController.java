package com.comada.cardapio.controller;

import java.net.URI;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.comada.cardapio.DTO.request.CategoriaRequestDTO;
import com.comada.cardapio.DTO.response.CategoriaResponseDTO;
import com.comada.cardapio.entity.Categoria;
import com.comada.cardapio.service.CategoriaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/categorias")
@RequiredArgsConstructor
public class CategoriaController {
	
	private final CategoriaService categoriaService;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoriaResponseDTO> findById(@PathVariable(name = "id") Long id) {
		
		return ResponseEntity.ok().body(categoriaService.findById(id));
	}
	
	@GetMapping
	public ResponseEntity<List<CategoriaResponseDTO>> findAll(){
		
		return ResponseEntity.ok().body(categoriaService.findAll());
	}
	
	@PostMapping
	public ResponseEntity<CategoriaResponseDTO> register(@RequestBody CategoriaRequestDTO categoriaRequestDTO, UriComponentsBuilder uriBuilder ){
		CategoriaResponseDTO categoriaResponseDTO = categoriaService.register(categoriaRequestDTO);
		
		URI uri = uriBuilder.path("/categoria/{id}").buildAndExpand(categoriaResponseDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(categoriaResponseDTO);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CategoriaResponseDTO> update(@RequestBody CategoriaRequestDTO categoriaRequestDTO, @PathVariable(name = "id")Long id){
		return ResponseEntity.ok().body(categoriaService.update(id,categoriaRequestDTO));
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> delete(@PathVariable(name = "id") Long id){
		return ResponseEntity.ok().body(categoriaService.delete(id));
	}
	
}
