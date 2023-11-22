package com.comandadigital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.ClienteLoginDTO;
import com.comandadigital.dtos.ClienteRegisterDTO;
import com.comandadigital.infra.security.AuthService;
import com.comandadigital.dtos.AuthDTO;
import com.comandadigital.services.ClienteServiceImplements;

import jakarta.validation.Valid;

@RestController
public class ClienteController {
	@Autowired
	ClienteServiceImplements clienteService;
	@Autowired
	AuthService authService;
	
	// Registrar
	@PostMapping("/login/registrar")
	public ResponseEntity register(@RequestBody @Valid ClienteRegisterDTO dto) {
		
		var cliente0 = clienteService.register(dto);
		
		if(cliente0 == null) {
			
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Já existe cadastro com o cpf ou telefone informado!!!");
		}
		
		return ResponseEntity.ok().body("Cadastro efetuado, faça o login para acessar nossos serviços");
	}
	
	
	

	
}
