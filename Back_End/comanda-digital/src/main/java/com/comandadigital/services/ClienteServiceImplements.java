package com.comandadigital.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.comandadigital.dtos.ClienteLoginDTO;
import com.comandadigital.dtos.ClienteRegisterDTO;
import com.comandadigital.dtos.ComandaRecordDTO;
import com.comandadigital.infra.security.TokenService;
import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.MesaModel;
import com.comandadigital.models.PerfilModel;
import com.comandadigital.models.StatusModel;
import com.comandadigital.repositories.ClienteRepository;
import com.comandadigital.repositories.MesaRepository;
import com.comandadigital.repositories.PerfilRepository;
import com.comandadigital.repositories.StatusRepository;

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
	@Autowired
	private MesaServiceImplements mesaService;
	@Autowired
	private ComandaServiceImplements comandaService;
	@Autowired
	private StatusRepository statusRepository;
	
	@Override
	public String login(@RequestBody @Valid ClienteLoginDTO dto) {
		
		// Validar se existe login
		if(repository.findByLogin(dto.login()) == null) {
			return "LoginNotFound";
		}
		
		Optional<MesaModel> mesaOptional = mesaRepository.findById(dto.mesa().getId());
		if (!mesaOptional.isPresent()|| mesaOptional.get().getStatus().getId().equals(12)) {
			return "MesaNotFound";
		}
		
		var userNameSenha = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
		var auth = this.authManager.authenticate(userNameSenha);
	    

		MesaModel mesa = mesaOptional.get();
		
		if (mesa.getStatus().getId() == 9) {
			 mesaService.atualizarStatusMesa(mesaOptional.get().getId(), 9, 10);
	    }
		 // Associar número da mesa ao cliente durante o login
        setarMesaCliente(dto.login(), mesa.getId());
    
        
     // Criar comanda para o cliente se n tiver comanda ativa   
        List<Integer> statusList = Arrays.asList(6, 7);
        if(comandaService.findComandaByCpf(dto.login(), statusList) == null ) {
        	 StatusModel defaultStatus = statusRepository.findById(6).orElseThrow(() -> new RuntimeException("Status não encontrado"));
             ClienteModel defaultCliente = (ClienteModel) repository.findByLogin(dto.login());
             ComandaRecordDTO comandaDTO = new ComandaRecordDTO(defaultStatus, defaultCliente);
             comandaService.register(comandaDTO);
        }
       
		
		return tokenService.generateTokenCliente((ClienteModel)auth.getPrincipal());	
		 
	}

	@Override
	public ClienteModel register(@RequestBody @Valid ClienteRegisterDTO dto) {
		if(repository.findByLogin(dto.cpf()) != null || repository.findByTelefone(dto.telefone()) != null) {
			return null;
		}
		String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha()); // criptografando senha
		PerfilModel perfilCliente = perfilRepository.findById(1).orElseThrow(() -> new RuntimeException("Perfil não encontrado")); // passando perfil
		
		ClienteModel newCliente = new ClienteModel(dto.cpf(),dto.nome(),encryptedPassword, dto.telefone(),perfilCliente);
		
		return this.repository.save(newCliente);
		
	}
	
	public void setarMesaCliente(String cpf, Integer nuMesa) {
		UserDetails clienteDetails  = repository.findByLogin(cpf);
		
		if(clienteDetails  != null) {
			 ClienteModel cliente = (ClienteModel) clienteDetails; // Converter UserDetails para ClienteModel
			 MesaModel mesaCliente = mesaRepository.findById(nuMesa).orElseThrow(() -> new RuntimeException("Mesa não encontrada"));
			 
			 cliente.setMesa(mesaCliente); // Associar a mesa ao cliente
	         repository.save(cliente);
		}
	}

}
