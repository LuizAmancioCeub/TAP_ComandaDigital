package com.comandadigital.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.comandadigital.dtos.GarcomLoginDTO;
import com.comandadigital.dtos.GarcomRegisterDTO;
import com.comandadigital.dtos.myValidations.Exceptions.NegocioException;
import com.comandadigital.infra.security.TokenService;
import com.comandadigital.models.GarcomModel;
import com.comandadigital.models.GerenteModel;
import com.comandadigital.models.PerfilModel;
import com.comandadigital.repositories.GarcomRepository;
import com.comandadigital.repositories.PerfilRepository;

import jakarta.validation.Valid;

@Service
public class GarcomService {
	@Autowired
	GarcomRepository garcomRepository;
	@Autowired
	PerfilRepository perfilRepository;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	TokenService tokenService;
	
	public String login(@RequestBody @Valid GarcomLoginDTO dto) {
		// Validar se existe login
		if(garcomRepository.findByLogin(dto.login().toUpperCase()) == null) {
			return "LoginNotFound";
		}
		
		var userNameSenha = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
		var auth = this.authManager.authenticate(userNameSenha);
		
		return tokenService.generateTokenGarcom((GarcomModel)auth.getPrincipal());
	}
	
	@Transactional
	public GarcomModel register(@Valid GarcomRegisterDTO dto) throws Exception {
		try {
			if(garcomRepository.findByCpf(dto.cpf()) != null) {
				throw new NegocioException("Já existe Garçom cadastrado com essas credenciais");
			}
			if(dto.perfil().getId() != PerfilModel.GARCOM) {
				throw new NegocioException("cadastro inválido");
			}
			String matricula = gerarMatricula();
			String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha());
			PerfilModel perfilGarcom = perfilRepository.findById(PerfilModel.GARCOM).orElseThrow(() -> new NegocioException("Perfil não encontrado"));
			
			GarcomModel newGarcom = new GarcomModel(dto.nome(), dto.telefone(),matricula, dto.cpf(), encryptedPassword, perfilGarcom);
			
			return this.garcomRepository.save(newGarcom);
		}catch (NegocioException e) {
			throw new NegocioException(e.getMessage());
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}
	
	public String gerarMatricula() {
		
	        Long maxMatricula = garcomRepository.findMaxMatricula();

	        if ( Objects.isNull(maxMatricula) || maxMatricula <= 1000) {
	            maxMatricula = 1001L;
	        } else {
	            maxMatricula++;
	        }
	        String newMatricula = "GA"+maxMatricula;

	        return newMatricula;
	}
}
