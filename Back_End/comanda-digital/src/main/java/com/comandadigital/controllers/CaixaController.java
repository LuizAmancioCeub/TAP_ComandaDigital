package com.comandadigital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.CaixaRegisterDTO;
import com.comandadigital.services.CaixaService;

import jakarta.validation.Valid;

@RestController
public class CaixaController {
	@Autowired
	CaixaService caixaService;
	// Registrar
		@PostMapping("caixa/registrar")
		public ResponseEntity register(@RequestBody @Valid CaixaRegisterDTO dto) throws Exception {
			var caixa = caixaService.register(dto);
			return ResponseEntity.ok().body("Cadastro do Caixa efetuado, faça o login para acessar os serviços");
		}
}
