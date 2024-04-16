package com.comandadigital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.AuthDTO;
import com.comandadigital.dtos.ClienteRegisterDTO;
import com.comandadigital.dtos.LoginDTO;
import com.comandadigital.services.VisitanteService;

import jakarta.validation.Valid;

@RestController
public class VisitanteController {
	@Autowired
	VisitanteService visitanteService;
	
	// Login
		@PostMapping("visitante/login")
		public ResponseEntity login(@RequestBody @Valid LoginDTO dto) throws Exception {
			String token = visitanteService.login(dto);
				
			return ResponseEntity.ok(new AuthDTO(token));
		}
		
	// Registrar
		@PostMapping("/visitante/registrar")
		public ResponseEntity register(@RequestBody @Valid ClienteRegisterDTO dto) {
			
			var visitante = visitanteService.register(dto);
			
			if(visitante == null) {
				
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Já existe cadastro com o cpf ou telefone informado!!!");
			}
			
			return ResponseEntity.ok().body("Cadastro efetuado, faça o login para acessar nossos serviços");
		}
}
