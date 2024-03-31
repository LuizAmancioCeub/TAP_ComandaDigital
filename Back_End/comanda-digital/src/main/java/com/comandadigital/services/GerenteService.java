package com.comandadigital.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.comandadigital.dtos.GerenteLoginDTO;
import com.comandadigital.dtos.GerenteRegisterDTO;
import com.comandadigital.dtos.myValidations.Exceptions.NegocioException;
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
		if(gerenteRepository.findByLogin(dto.login().toUpperCase()) == null) {
			return "LoginNotFound";
		}
		
		var userNameSenha = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
		var auth = this.authManager.authenticate(userNameSenha);
		
		return tokenService.generateTokenGerente((GerenteModel)auth.getPrincipal());
	}

	public GerenteModel register(@RequestBody @Valid GerenteRegisterDTO dto) {
		if(gerenteRepository.findByCpf(dto.cpf()) != null) {
			throw new NegocioException("Já existe Gerente cadastrado com essas credenciais");
		}
		String matricula = gerarMatricula();
		String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha());
		PerfilModel perfilGerente = perfilRepository.findById(PerfilModel.GERENTE).orElseThrow(() -> new NegocioException("Perfil não encontrado"));
		
		GerenteModel newGerente = new GerenteModel(dto.nome(), dto.telefone(),matricula, dto.cpf(), encryptedPassword, perfilGerente);
		
		return this.gerenteRepository.save(newGerente);
	}
	
	public String gerarMatricula() {
		
	        Long maxMatricula = gerenteRepository.findMaxMatricula();

	        if ( Objects.isNull(maxMatricula) || maxMatricula <= 1000) {
	            maxMatricula = 1001L;
	        } else {
	            maxMatricula++;
	        }
	        String newMatricula = "GE"+maxMatricula;

	        return newMatricula;
	}
}
