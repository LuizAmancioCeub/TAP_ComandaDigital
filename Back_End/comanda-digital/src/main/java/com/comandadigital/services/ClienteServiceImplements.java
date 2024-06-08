package com.comandadigital.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.comandadigital.dtos.ClienteLoginDTO;
import com.comandadigital.dtos.ClienteRegisterDTO;
import com.comandadigital.dtos.ClienteUpdateDTO;
import com.comandadigital.dtos.ComandaRecordDTO;
import com.comandadigital.dtos.myValidations.Exceptions.NegocioException;
import com.comandadigital.infra.security.TokenService;
import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.MesaModel;
import com.comandadigital.models.PerfilModel;
import com.comandadigital.models.StatusModel;
import com.comandadigital.models.projection.ClienteProjection;
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
	@Transactional
	public String login(@RequestBody @Valid ClienteLoginDTO dto) throws Exception {
		ClienteModel cliente = (ClienteModel) repository.findByLogin(dto.login());
		// Validar se existe login
		if(repository.findByLogin(cliente.getLogin()) == null) {
			throw new NegocioException("Usuário não encontrado");
		}
		
		if(cliente.getPerfil().getId() == PerfilModel.CLIENTE) {
			if(dto.mesa().getId() == null) {
				throw new NegocioException("Mesa precisa ser preenchida");
			}
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
	        if(!comandaService.existsComandaByCpf(dto.login(), statusList)) {
	        	 StatusModel defaultStatus = statusRepository.findById(StatusModel.ABERTA).orElseThrow(() -> new RuntimeException("Status não encontrado"));
	             ClienteModel defaultCliente = (ClienteModel) repository.findByLogin(dto.login());
	             ComandaRecordDTO comandaDTO = new ComandaRecordDTO(defaultStatus, defaultCliente, LocalDateTime.now(), LocalDateTime.now());
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
	@Transactional
	public ClienteModel register(@RequestBody @Valid ClienteRegisterDTO dto) {
		if(repository.findByLogin(dto.cpf()) != null || repository.findByTelefone(dto.telefone()) != null || repository.findByEmail(dto.email()) != null) {
			throw new NegocioException("Já existe cadastro com as credenciais informadas");
		}
		String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha()); // criptografando senha
		PerfilModel perfilCliente = perfilRepository.findById(1).orElseThrow(() -> new NegocioException("Perfil não encontrado para cadastrar cliente")); // passando perfil
		
		ClienteModel newCliente = new ClienteModel(dto.cpf(),dto.nome(),encryptedPassword, dto.telefone(),dto.email(),perfilCliente);
		
		return this.repository.save(newCliente);
		
	}
	
	public void setarMesaCliente(String cpf, Integer nuMesa) throws Exception {
		try {
			UserDetails clienteDetails  = repository.findByLogin(cpf);
			
			if(clienteDetails  != null) {
				 ClienteModel cliente = (ClienteModel) clienteDetails; // Converter UserDetails para ClienteModel
				 MesaModel mesaCliente = mesaRepository.findById(nuMesa).orElseThrow(() -> new NegocioException("Mesa não encontrada"));
				 
				 cliente.setMesa(mesaCliente); // Associar a mesa ao cliente
		         repository.save(cliente);
		         if(mesaCliente.getStatus().getId().equals(StatusModel.LIVRE)) {
		        	 StatusModel status = statusRepository.findById(StatusModel.OCUPADA).orElseThrow(() -> new RuntimeException("Status não encontrado"));
		        	 mesaCliente.setStatus(status);
		        	 mesaRepository.save(mesaCliente);
				 }
			}
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}
	
	@Transactional
	public void alterarMesa(Integer novaMesa, Integer mesaAtual, String cpf) throws Exception {
		if(novaMesa.equals(mesaAtual)) {
			throw new NegocioException("Você ja está vinculado a esta mesa !");
		}
		setarMesaCliente(cpf, novaMesa);
	}
	
	@Transactional
	public ClienteProjection alterarDados(ClienteUpdateDTO dto) throws Exception {
		try {
			if(repository.findByLogin(dto.cpf()) == null) {
				throw new NegocioException("Usuário não encontrado");
			}
			ClienteModel cliente = (ClienteModel) repository.findByLogin(dto.cpf());
			validarTelefoneEmail(dto, cliente);
			cliente.setNome(dto.nome());
			repository.save(cliente);
			return converterCliente(cliente);
		}catch (NegocioException e) {
			throw new NegocioException(e.getMessage());
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	private ClienteProjection converterCliente(ClienteModel cm) {
		ClienteProjection cp = new ClienteProjection();
		cp.setLogin(cm.getLogin());
		cp.setNome(cm.getNome());
		cp.setTelefone(cm.getTelefone());
		cp.setEmail(cm.getEmail());
		cp.setPerfil(cm.getPerfil().getId());
		return cp;
	}
	
	private void validarTelefoneEmail(ClienteUpdateDTO dto, ClienteModel cliente) {
		if(!dto.telefone().equals(cliente.getTelefone())) {
			if(repository.findByTelefone(dto.telefone()) != null) {
				throw new NegocioException("Já existe cadastro com esse número de celular");
			}
			cliente.setTelefone(dto.telefone());
		}
		
		if(!dto.email().equals(cliente.getEmail())) {
			if(repository.findByEmail(dto.email()) != null) {
				throw new NegocioException("Já existe cadastro com o email informado");
			}
			cliente.setEmail(dto.email());
		}
	}

}
