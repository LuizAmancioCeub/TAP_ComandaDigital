package com.comandadigital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.CozinhaLoginDTO;
import com.comandadigital.dtos.CozinhaRegisterDTO;
import com.comandadigital.dtos.AuthDTO;
import com.comandadigital.services.CozinhaServiceImplements;

import jakarta.validation.Valid;

@RestController
public class CozinhaController {
	
	@Autowired
	CozinhaServiceImplements cozinhaService;
	
	// Registrar
	@PostMapping("cozinha/registrar")
	public ResponseEntity register(@RequestBody @Valid CozinhaRegisterDTO dto) {
		var cozinha0 = cozinhaService.register(dto);
		if(cozinha0 == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Já existe Cozinha cadastrada com essas credenciais!!!");
		}
		return ResponseEntity.ok().body("Cadastro da Cozinha efetuado, faça o login para acessar os serviços");
	}
	
}
