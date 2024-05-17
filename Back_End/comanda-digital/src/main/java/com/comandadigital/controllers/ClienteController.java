package com.comandadigital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.AlterarMesaDTO;
import com.comandadigital.dtos.ClienteRegisterDTO;
import com.comandadigital.dtos.ClienteUpdateDTO;
import com.comandadigital.dtos.myValidations.Exceptions.NegocioException;
import com.comandadigital.infra.security.AuthService;
import com.comandadigital.models.projection.ClienteProjection;
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
		clienteService.register(dto);
		return ResponseEntity.ok().body("Cadastro efetuado, faça o login para acessar nossos serviços");
	}
	
	@PutMapping("/mesa/alterar")
	public ResponseEntity alterarMesa(@RequestBody @Valid AlterarMesaDTO dto) throws Exception {
		clienteService.alterarMesa( dto.novaMesa(), dto.mesaAtual(), dto.cpf());
		return ResponseEntity.ok().body("Mesa alterado com sucesso");
	}
	
	@PutMapping("/cliente")
	public ResponseEntity alterarDados(@RequestBody @Valid ClienteUpdateDTO dto)throws Exception{
		clienteService.alterarDados(dto);
		return  ResponseEntity.ok().body("Seu perfil foi atualizado");
	}
	

	
}
