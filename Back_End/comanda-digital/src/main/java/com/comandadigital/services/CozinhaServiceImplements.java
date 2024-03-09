package com.comandadigital.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.comandadigital.dtos.CozinhaLoginDTO;
import com.comandadigital.dtos.CozinhaRegisterDTO;
import com.comandadigital.infra.security.TokenService;
import com.comandadigital.models.CozinhaModel;
import com.comandadigital.models.PerfilModel;
import com.comandadigital.repositories.CozinhaRepository;
import com.comandadigital.repositories.PerfilRepository;

import jakarta.validation.Valid;

@Service
public class CozinhaServiceImplements implements CozinhaService {
	
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	TokenService tokenService;
	@Autowired
	private CozinhaRepository cozinhaRepository;
	@Autowired
	private PerfilRepository perfilRepository;
	
	@Override
	public String login(@RequestBody @Valid CozinhaLoginDTO dto) {
		// Validar se existe login
		if(cozinhaRepository.findByLogin(dto.login()) == null) {
			return "LoginNotFound";
		}
		
		var userNameSenha = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
		var auth = this.authManager.authenticate(userNameSenha);
		
		return tokenService.generateTokenCozinha((CozinhaModel)auth.getPrincipal());
	}

	@Override
	public CozinhaModel register(@RequestBody @Valid CozinhaRegisterDTO dto) {
		if(cozinhaRepository.findByLogin(dto.login()) != null) {
			return null;
		}
		String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha());
		PerfilModel perfilCliente = perfilRepository.findById(PerfilModel.COZINHA).orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
		
		CozinhaModel newCozinha = new CozinhaModel(dto.login(), encryptedPassword, perfilCliente);
		
		return this.cozinhaRepository.save(newCozinha);
	}

}
