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
import com.comandadigital.dtos.myValidations.Exceptions.NegocioException;
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
	private TokenService tokenService;
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
		ClienteModel cliente = (ClienteModel) repository.findByLogin(dto.login());
		// Validar se existe login
		if(repository.findByLogin(cliente.getLogin()) == null) {
			throw new NegocioException("Usuário não encontrado");
		}
		
		if(cliente.getPerfil().getId() == PerfilModel.CLIENTE) {
			Optional<MesaModel> mesaOptional = mesaRepository.findById(dto.mesa().getId());
			if (!mesaOptional.isPresent()|| mesaOptional.get().getStatus().getId().equals(StatusModel.INDISPONIVEL)) {
				throw new NegocioException("Mesa indisponível");
			}
			
			var userNameSenha = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
			var auth = this.authManager.authenticate(userNameSenha);
		    

			MesaModel mesa = mesaOptional.get();
			
			if (mesa.getStatus().getId().equals(StatusModel.LIVRE)) {
				 mesaService.atualizarStatusMesa(mesaOptional.get().getId(), StatusModel.LIVRE, StatusModel.OCUPADA);
		    }
			 // Associar número da mesa ao cliente durante o login
	        setarMesaCliente(dto.login(), mesa.getId());
	    
	        
	     // Criar comanda para o cliente se n tiver comanda ativa   
	        List<Integer> statusList = Arrays.asList(StatusModel.ABERTA, StatusModel.AGUARDANDO_PAGAMENTO);
	        if(comandaService.findComandaByCpf(dto.login(), statusList) == null ) {
	        	 StatusModel defaultStatus = statusRepository.findById(StatusModel.ABERTA).orElseThrow(() -> new RuntimeException("Status não encontrado"));
	             ClienteModel defaultCliente = (ClienteModel) repository.findByLogin(dto.login());
	             ComandaRecordDTO comandaDTO = new ComandaRecordDTO(defaultStatus, defaultCliente);
	             comandaService.register(comandaDTO);
	        }
	       
			
			return tokenService.generateTokenCliente((ClienteModel)auth.getPrincipal());
		}
		else if(cliente.getPerfil().getId() == PerfilModel.VISITANTE) {
			// visitante
			var userNameSenha = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
			var auth = this.authManager.authenticate(userNameSenha);
			return tokenService.generateTokenCliente((ClienteModel)auth.getPrincipal());
		}
		
		else {
			return null;
		}
		 
	}

	@Override
	public ClienteModel register(@RequestBody @Valid ClienteRegisterDTO dto) {
		validarCampos(dto);
		if(repository.findByLogin(dto.cpf()) != null || repository.findByTelefone(dto.telefone()) != null) {
			throw new NegocioException("Já existe cadastro com o CPF ou Telefone informados");
		}
		String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha()); // criptografando senha
		PerfilModel perfilCliente = perfilRepository.findById(1).orElseThrow(() -> new NegocioException("Perfil não encontrado para cadastrar cliente")); // passando perfil
		
		ClienteModel newCliente = new ClienteModel(dto.cpf(),dto.nome(),encryptedPassword, dto.telefone(),perfilCliente);
		
		return this.repository.save(newCliente);
		
	}
	
	public void setarMesaCliente(String cpf, Integer nuMesa) {
		UserDetails clienteDetails  = repository.findByLogin(cpf);
		
		if(clienteDetails  != null) {
			 ClienteModel cliente = (ClienteModel) clienteDetails; // Converter UserDetails para ClienteModel
			 MesaModel mesaCliente = mesaRepository.findById(nuMesa).orElseThrow(() -> new NegocioException("Mesa não encontrada"));
			 
			 cliente.setMesa(mesaCliente); // Associar a mesa ao cliente
	         repository.save(cliente);
		}
	}
	
	public void validarCampos(ClienteRegisterDTO dto) {
		
	}

}
