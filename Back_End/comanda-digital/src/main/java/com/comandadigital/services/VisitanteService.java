package com.comandadigital.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.comandadigital.dtos.ClienteLoginDTO;
import com.comandadigital.dtos.ClienteRegisterDTO;
import com.comandadigital.dtos.CozinhaLoginDTO;
import com.comandadigital.dtos.GerenteLoginDTO;
import com.comandadigital.dtos.LoginDTO;
import com.comandadigital.dtos.myValidations.Exceptions.NegocioException;
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
	ClienteService clienteService;
	@Autowired
	PerfilRepository perfilRepository;
	
	public ClienteModel register(@RequestBody @Valid ClienteRegisterDTO dto) {
		if(clienteRepository.findByLogin(dto.cpf()) != null || clienteRepository.findByTelefone(dto.telefone()) != null) {
			return null;
		}
		String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha()); // criptografando senha
		PerfilModel perfilCliente = perfilRepository.findById(PerfilModel.VISITANTE).orElseThrow(() -> new RuntimeException("Perfil não encontrado")); // passando perfil
		
		ClienteModel newCliente = new ClienteModel(dto.cpf(),dto.nome(),encryptedPassword, dto.telefone(),perfilCliente);
		
		return this.clienteRepository.save(newCliente);
		
	}
	
	public String login(@RequestBody @Valid LoginDTO dto) throws Exception {
		try {
			if(clienteRepository.findByLogin(dto.login()) != null) {
				ClienteLoginDTO clienteDTO = new ClienteLoginDTO(dto.login(), dto.senha(), dto.mesa());
				var visitante = clienteService.login(clienteDTO);
				return visitante;
			}else {
				String encryptedPassword = new BCryptPasswordEncoder().encode("visitante"); // criptografando senha
				PerfilModel perfilVisitante = perfilRepository.findById(PerfilModel.VISITANTE).orElseThrow(() -> new NegocioException("Perfil não encontrado para cadastrar visitante")); // passando perfil
				
				ClienteModel newVisitante = new ClienteModel("visitante12","visitante",encryptedPassword, "visitante12",perfilVisitante);
				this.clienteRepository.save(newVisitante);
				ClienteLoginDTO visitanteDTO = new ClienteLoginDTO(newVisitante.getLogin(), newVisitante.getSenha(), newVisitante.getMesa());
				var visitante = clienteService.login(visitanteDTO);
				return visitante;
			}
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
		 
	}
}
