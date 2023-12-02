package com.comandadigital.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.comandadigital.dtos.ClienteRegisterDTO;
import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.PerfilModel;
import com.comandadigital.repositories.ClienteRepository;
import com.comandadigital.repositories.PerfilRepository;

import jakarta.validation.Valid;

@Service
public class VisitanteService {
	
	@Autowired
	ClienteRepository clienteRepository;
	@Autowired
	PerfilRepository perfilRepository;
	
	public ClienteModel register(@RequestBody @Valid ClienteRegisterDTO dto) {
		if(clienteRepository.findByLogin(dto.cpf()) != null || clienteRepository.findByTelefone(dto.telefone()) != null) {
			return null;
		}
		String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha()); // criptografando senha
		PerfilModel perfilCliente = perfilRepository.findById(2).orElseThrow(() -> new RuntimeException("Perfil não encontrado")); // passando perfil
		
		ClienteModel newCliente = new ClienteModel(dto.cpf(),dto.nome(),encryptedPassword, dto.telefone(),perfilCliente);
		
		return this.clienteRepository.save(newCliente);
		
	}
}
