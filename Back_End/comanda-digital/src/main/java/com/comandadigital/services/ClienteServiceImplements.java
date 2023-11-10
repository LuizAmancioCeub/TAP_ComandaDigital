package com.comandadigital.services;

import java.util.Optional;

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
import com.comandadigital.models.MesaModel;
import com.comandadigital.models.PerfilModel;
import com.comandadigital.repositories.ClienteRepository;
import com.comandadigital.repositories.MesaRepository;
import com.comandadigital.repositories.PerfilRepository;

import jakarta.validation.Valid;

@Service
public class ClienteServiceImplements implements ClienteService {
	
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private ClienteRepository repository;
	@Autowired
	TokenService tokenService;
	@Autowired
	private PerfilRepository perfilRepository;
	@Autowired
	private MesaRepository mesaRepository;
	
	@Override
	public String login(@RequestBody @Valid ClienteLoginDTO dto) {
		
		// Validar se existe login
		if(repository.findByCpf(dto.cpf()) == null) {
			return "LoginNotFound";
		}
		
		Optional<MesaModel> mesaOptional = mesaRepository.findById(dto.mesa().getId());
		if (!mesaOptional.isPresent()) {
			return "MesaNotFound";
		}
		MesaModel mesa = mesaOptional.get();
		if(mesa.getStatus().getId().equals(12) ) {
			return "MesaNotFound";
		}
		
		var userNameSenha = new UsernamePasswordAuthenticationToken(dto.cpf(), dto.senha());
		var auth = this.authManager.authenticate(userNameSenha);

		return tokenService.generateToken((ClienteModel)auth.getPrincipal());	
	}

	@Override
	public ClienteModel register(@RequestBody @Valid ClienteRegisterDTO dto) {
		if(repository.findByCpf(dto.cpf()) != null || repository.findByTelefone(dto.telefone()) != null) {
			return null;
		}
		String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha()); // criptografando senha
		PerfilModel perfilCliente = perfilRepository.findById(1).orElseThrow(() -> new RuntimeException("Perfil não encontrado")); // passando perfil
		
		ClienteModel newCliente = new ClienteModel(dto.cpf(),dto.nome(),encryptedPassword, dto.telefone(),perfilCliente);
		
		return this.repository.save(newCliente);
		
	}

}
