package com.comandadigital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.AuthDTO;
import com.comandadigital.dtos.LoginDTO;
import com.comandadigital.services.LoginService;

import jakarta.validation.Valid;

@RestController
public class LoginController {
	@Autowired
	LoginService loginService;
	
	// Login
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid LoginDTO dto) {
		String token = loginService.login(dto);
		
		if(token.equals("MesaNotFound")) {
				
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mesa Indisponível");
				
		}else if(token.equals("LoginNotFound")){
				
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário ainda não cadastrado");
		}
			
		return ResponseEntity.ok(new AuthDTO(token));
	}
}
