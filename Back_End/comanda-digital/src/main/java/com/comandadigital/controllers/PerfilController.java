package com.comandadigital.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.models.PerfilModel;
import com.comandadigital.repositories.PerfilRepository;

@RestController
public class PerfilController {
	
	@Autowired
	PerfilRepository perfilRepository;
	
	@GetMapping("/perfis")
	public ResponseEntity<List<PerfilModel>> getAllPerfil(){
		List<PerfilModel> perfilList = perfilRepository.findAll();
		if(perfilList.isEmpty()) {
			return null; 
		}
		return ResponseEntity.status(HttpStatus.OK).body(perfilList);
	}
}
