package com.comandadigital.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.GarcomRegisterDTO;
import com.comandadigital.models.GarcomModel;
import com.comandadigital.models.MesaModel;
import com.comandadigital.models.projection.Garcomprojection;
import com.comandadigital.services.GarcomService;

import jakarta.validation.Valid;

@RestController
public class GarcomController {
	
	@Autowired
	GarcomService garcomService;
	
	// Registrar
			@PostMapping("garcom/registrar")
			public ResponseEntity register(@RequestBody @Valid GarcomRegisterDTO dto) throws Exception {
				var garcom = garcomService.register(dto);
				return ResponseEntity.ok().body("Cadastro do Garçom efetuado, faça o login para acessar os serviços."+ "\nMatrícula: "+garcom.getLogin());
			}
			
			@GetMapping("/garcom")
			public ResponseEntity<List<Garcomprojection>> getAllGarcons(){
				List<Garcomprojection> garcons = garcomService.getAll();
				return ResponseEntity.status(HttpStatus.OK).body(garcons);
			}
}
