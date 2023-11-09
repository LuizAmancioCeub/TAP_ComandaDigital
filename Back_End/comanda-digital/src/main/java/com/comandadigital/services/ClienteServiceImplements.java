package com.comandadigital.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.comandadigital.dtos.ClienteLoginDTO;
import com.comandadigital.dtos.ClienteRegisterDTO;
import com.comandadigital.infra.security.TokenService;
import com.comandadigital.models.ClienteModel;
import com.comandadigital.repositories.ClienteRepository;

import jakarta.validation.Valid;

@Service
public class ClienteServiceImplements implements ClienteService {
	
	@Autowired
	AuthenticationManager authManager;
	@Autowired
	ClienteRepository repository;
	@Autowired
	TokenService tokenService;
	
	@Override
	public String login(ClienteLoginDTO dto) {
		var userNameSenha = new UsernamePasswordAuthenticationToken(dto.cpf(), dto.senha());
		var auth = this.authManager.authenticate(userNameSenha);
		return tokenService.generateToken((ClienteModel)auth.getPrincipal());
	}

	@Override
	public ClienteModel register(@RequestBody @Valid ClienteRegisterDTO dto) {
		if(repository.findByLogin(dto.cpf()) != null) {
			return null;
		}
		String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha());
		ClienteModel newCliente = new ClienteModel(dto.cpf(),dto.nome(),encryptedPassword, dto.telefone(),dto.perfil(),dto.mesa());
		
		return this.repository.save(newCliente);
		
	}

}
