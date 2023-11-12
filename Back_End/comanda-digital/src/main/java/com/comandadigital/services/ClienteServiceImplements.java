package com.comandadigital.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
	StatusRepository statusRepository;
	@Autowired
	ClienteRepository clienteRepository;
	
	@Override
	public String login(@RequestBody @Valid ClienteLoginDTO dto) {
		
		// Validar se existe login
		if(repository.findByCpf(dto.cpf()) == null) {
			return "LoginNotFound";
		}
		
		Optional<MesaModel> mesaOptional = mesaRepository.findById(dto.mesa().getId());
		if (!mesaOptional.isPresent()|| mesaOptional.get().getStatus().getId().equals(12)) {
			return "MesaNotFound";
		}
		
		var userNameSenha = new UsernamePasswordAuthenticationToken(dto.cpf(), dto.senha());
		var auth = this.authManager.authenticate(userNameSenha);
	    
//		String cpfDoUsuarioAutenticado = ((ClienteModel) auth.getPrincipal()).getCpf();
//		List<PedidoModel> pedidosDoCliente = pedidoService.findPedidoByCpf(cpfDoUsuarioAutenticado);

		MesaModel mesa = mesaOptional.get();
		
		if (mesa.getStatus().getId() == 9) {
			 mesaService.atualizarStatusMesa(mesaOptional.get().getId(), 9, 10);
	    }
		 // Associar número da mesa ao cliente durante o login
        setarMesaCliente(dto.cpf(), mesa.getId());
    
        
     // Criar comanda para o cliente se n tiver comanda ativa
        if(comandaService.findComandaByCpf(dto.cpf(),6) == null) {
        	 StatusModel defaultStatus = statusRepository.findById(6).orElseThrow(() -> new RuntimeException("Status não encontrado"));
             ClienteModel defaultCliente = (ClienteModel) clienteRepository.findByCpf(dto.cpf());
             ComandaRecordDTO comandaDTO = new ComandaRecordDTO(defaultStatus, defaultCliente);
             comandaService.register(comandaDTO);
        }
       
		
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
	
	public void setarMesaCliente(String cpf, Integer nuMesa) {
		UserDetails clienteDetails  = repository.findByCpf(cpf);
		
		if(clienteDetails  != null) {
			 ClienteModel cliente = (ClienteModel) clienteDetails; // Converter UserDetails para ClienteModel
			 MesaModel mesaCliente = mesaRepository.findById(nuMesa).orElseThrow(() -> new RuntimeException("Mesa não encontrada"));
			 
			 cliente.setMesa(mesaCliente); // Associar a mesa ao cliente
	         repository.save(cliente);
		}
	}

}
