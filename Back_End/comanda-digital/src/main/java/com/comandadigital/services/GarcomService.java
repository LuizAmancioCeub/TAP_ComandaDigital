package com.comandadigital.services;

import java.util.ArrayList;
import java.util.List;
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
import com.comandadigital.models.projection.Garcomprojection;
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
	
	public String login(@Valid GarcomLoginDTO dto) throws Exception {
		try {
			// Validar se existe login
			if(garcomRepository.findByLogin(dto.login().toUpperCase()) == null) {
				return "LoginNotFound";
			}
			
			var userNameSenha = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
			var auth = this.authManager.authenticate(userNameSenha);
			
			return tokenService.generateTokenGarcom((GarcomModel)auth.getPrincipal());
		}catch (Exception e) {
			throw new NegocioException(e.getMessage());
		}
	
	}
	
	@Transactional
	public GarcomModel register(@Valid GarcomRegisterDTO dto) throws Exception {
		try {
			if(garcomRepository.findByCpf(dto.cpf()) != null || garcomRepository.findByEmail(dto.email()) != null ) {
				throw new NegocioException("Já existe Garçom cadastrado com essas credenciais");
			}
			if(dto.perfil().getId() != PerfilModel.GARCOM) {
				throw new NegocioException("cadastro inválido");
			}
			String matricula = gerarMatricula();
			String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha());
			PerfilModel perfilGarcom = perfilRepository.findById(PerfilModel.GARCOM).orElseThrow(() -> new NegocioException("Perfil não encontrado"));
			
			GarcomModel newGarcom = new GarcomModel(dto.nome(), dto.telefone(),dto.email(),matricula, dto.cpf(), encryptedPassword, perfilGarcom);
			
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
	
	public List<Garcomprojection>getAll(){
		List<GarcomModel> garcom0 = garcomRepository.findAll();
		List<Garcomprojection> garcom = new ArrayList<>();
		if(!garcom0.isEmpty()) {
			for(GarcomModel g : garcom0) {
				Garcomprojection gar = new Garcomprojection();
				gar.setId(g.getId());
				gar.setMatricula(g.getLogin());
				gar.setCpf(g.getCpf());
				gar.setNome(g.getNome());
				gar.setTelefone(g.getTelefone());
				gar.setEmail(g.getEmail());
				garcom.add(gar);
			}
			return garcom;
		}
		return garcom;
	}
}
