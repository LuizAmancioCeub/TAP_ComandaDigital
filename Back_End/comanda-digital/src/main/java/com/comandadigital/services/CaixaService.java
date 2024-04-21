package com.comandadigital.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.comandadigital.dtos.CaixaLoginDTO;
import com.comandadigital.dtos.CaixaRegisterDTO;
import com.comandadigital.dtos.myValidations.Exceptions.NegocioException;
import com.comandadigital.infra.security.TokenService;
import com.comandadigital.models.CaixaModel;
import com.comandadigital.models.PerfilModel;
import com.comandadigital.repositories.CaixaRepository;
import com.comandadigital.repositories.PerfilRepository;

import jakarta.validation.Valid;

@Service
public class CaixaService {
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	TokenService tokenService;
	@Autowired
	private CaixaRepository caixaRepository;
	@Autowired
	private PerfilRepository perfilRepository;
	
	public String login(@RequestBody @Valid CaixaLoginDTO dto) {
		// Validar se existe login
		if(caixaRepository.findByLogin(dto.login()) == null) {
			return "LoginNotFound";
		}
		
		var userNameSenha = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
		var auth = this.authManager.authenticate(userNameSenha);
		
		return tokenService.generateTokenCaixa((CaixaModel)auth.getPrincipal());
	}
	
	@Transactional
	public CaixaModel register(@RequestBody @Valid CaixaRegisterDTO dto) throws Exception {
		try {
			if(caixaRepository.findByLogin(dto.login()) != null) {
				throw new NegocioException("Já existe Caixa cadastrado com essas credenciais");
			}
			String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha());
			PerfilModel perfilCliente = perfilRepository.findById(PerfilModel.CAIXA).orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
			
			CaixaModel newCaixa = new CaixaModel(dto.login(), encryptedPassword, perfilCliente);
			
			return this.caixaRepository.save(newCaixa);
		}catch (NegocioException e) {
			throw new NegocioException(e.getMessage());
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
