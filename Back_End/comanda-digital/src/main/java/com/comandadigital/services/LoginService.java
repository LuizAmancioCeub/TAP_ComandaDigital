package com.comandadigital.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.comandadigital.dtos.ClienteLoginDTO;
import com.comandadigital.dtos.CozinhaLoginDTO;
import com.comandadigital.dtos.GerenteLoginDTO;
import com.comandadigital.dtos.LoginDTO;
import com.comandadigital.infra.security.TokenService;
import com.comandadigital.repositories.ClienteRepository;
import com.comandadigital.repositories.CozinhaRepository;
import com.comandadigital.repositories.GerenteRepository;

import jakarta.validation.Valid;

@Service
public class LoginService {
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private CozinhaRepository cozinhaRepository;
	@Autowired
	private GerenteRepository gerenteRepository;
	@Autowired
	TokenService tokenService;
	@Autowired
	private CozinhaServiceImplements cozinhaService;
	@Autowired
	private ClienteServiceImplements clienteService;
	@Autowired
	private GerenteService gerenteService;
	
	public String login(@RequestBody @Valid LoginDTO dto) {
		
		if(clienteRepository.findByLogin(dto.login()) != null) {
			ClienteLoginDTO clienteDTO = new ClienteLoginDTO(dto.login(), dto.senha(), dto.mesa());
			var cliente = clienteService.login(clienteDTO);
			return cliente;
		}

		if(gerenteRepository.findByLogin(dto.login()) != null) {
			GerenteLoginDTO gerenteDTO = new GerenteLoginDTO(dto.login(), dto.senha());
			var gerente = gerenteService.login(gerenteDTO);
			return gerente;
		}
		
		if(cozinhaRepository.findByLogin(dto.login()) != null) {
			CozinhaLoginDTO cozinhaDTO = new CozinhaLoginDTO(dto.login(), dto.senha());
			var cozinha = cozinhaService.login(cozinhaDTO);
			return cozinha;
		}
		
		// Validar se existe login
			return "LoginNotFound";	
		 
	}
}
