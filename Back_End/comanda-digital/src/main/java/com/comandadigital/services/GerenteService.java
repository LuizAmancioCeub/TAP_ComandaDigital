package com.comandadigital.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.comandadigital.dtos.GerenteLoginDTO;
import com.comandadigital.dtos.GerenteRegisterDTO;
import com.comandadigital.infra.security.TokenService;
import com.comandadigital.models.GerenteModel;
import com.comandadigital.models.PerfilModel;
import com.comandadigital.repositories.GerenteRepository;
import com.comandadigital.repositories.PerfilRepository;

import jakarta.validation.Valid;

@Service
public class GerenteService {
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	TokenService tokenService;
	@Autowired
	private GerenteRepository gerenteRepository;
	@Autowired
	private PerfilRepository perfilRepository;
	
	public String login(@RequestBody @Valid GerenteLoginDTO dto) {
		// Validar se existe login
		if(gerenteRepository.findByLogin(dto.login()) == null) {
			return "LoginNotFound";
		}
		
		var userNameSenha = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
		var auth = this.authManager.authenticate(userNameSenha);
		
		return tokenService.generateTokenGerente((GerenteModel)auth.getPrincipal());
	}

	public GerenteModel register(@RequestBody @Valid GerenteRegisterDTO dto) {
		if(gerenteRepository.findByLogin(dto.login()) != null) {
			return null;
		}
		String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha());
		PerfilModel perfilGerente = perfilRepository.findById(PerfilModel.GERENTE).orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
		
		GerenteModel newGerente = new GerenteModel(dto.nome(), dto.telefone(),dto.login(), encryptedPassword, perfilGerente);
		
		return this.gerenteRepository.save(newGerente);
	}
}
