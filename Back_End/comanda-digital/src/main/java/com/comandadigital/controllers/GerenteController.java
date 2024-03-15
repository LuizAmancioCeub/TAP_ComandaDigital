package com.comandadigital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.GerenteRegisterDTO;
import com.comandadigital.infra.security.AuthService;
import com.comandadigital.services.GerenteService;

import jakarta.validation.Valid;

@RestController
public class GerenteController {
	@Autowired
	GerenteService gerenteService;
	@Autowired
	AuthService authService;
	
	
	// Registrar
		@PostMapping("gerente/registrar")
		public ResponseEntity register(@RequestBody @Valid GerenteRegisterDTO dto) {
			var gerente0 = gerenteService.register(dto);
			if(gerente0 == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Já existe Gerente cadastrada com essas credenciais!!!");
			}
			return ResponseEntity.ok().body("Cadastro do Gerente efetuado, faça o login para acessar os serviços");
		}
}
