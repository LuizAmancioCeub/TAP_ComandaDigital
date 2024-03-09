package com.comandadigital.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.comandadigital.controllers.PedidoController;
import com.comandadigital.controllers.StatusController;
import com.comandadigital.dtos.ComandaRecordDTO;
import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.ComandaModel;
import com.comandadigital.models.StatusModel;
import com.comandadigital.repositories.ClienteRepository;
import com.comandadigital.repositories.ComandaRepository;
import com.comandadigital.repositories.StatusRepository;

@Service
public class ComandaServiceImplements implements ComandaService {
	@Autowired
	ComandaRepository comandaRepository;
	@Autowired
	StatusRepository statusRepository;
	@Autowired
	ClienteRepository clienteRepository;
	
	// Falta colocar Hateoas
	
	@Override
	public Optional<ComandaModel> findById(Integer id) {
		Optional<ComandaModel> comanda = comandaRepository.findById(id);
		if(comanda.isEmpty()) {
			throw new RuntimeException("Comanda não encontrada");
		}
		
		comanda.get().getStatus().add(linkTo(methodOn(StatusController.class).getAllStatus()).withSelfRel());
		return comanda;
	}

	@Override
	public List<ComandaModel> findAll() {
		List<ComandaModel> comandas = comandaRepository.findAll();
		if(comandas.isEmpty()) {
			throw new RuntimeException(" Não foram encontrada Comandas");
		}
		
		return comandas;
	}
	
	@Override
	public ComandaModel register(ComandaRecordDTO comandaDTO) {
		var comandaModel = new ComandaModel();
		BeanUtils.copyProperties(comandaDTO, comandaModel);
		
		// consultando o status inicial do item
		StatusModel defaultStatus = statusRepository.findById(StatusModel.ABERTA).orElseThrow(() -> new RuntimeException("Status não encontrado"));
		comandaModel.setStatus(defaultStatus);
		// valor incial da comanda
		comandaModel.setValorTotal(0.0);
		
		return this.comandaRepository.save(comandaModel);
	}

	@Override
	public ComandaModel update(Integer id, ComandaRecordDTO comandaDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete(Integer id) {
		Optional<ComandaModel> comanda0 = comandaRepository.findById(id);
		if(comanda0.isEmpty()) {
			throw new RuntimeException("Comanda não encontrada");
		}
		ComandaModel comandaDelete = comanda0.get();
		comandaRepository.delete(comandaDelete);
		return "Comanda do cliente "+comandaDelete.getCliente().getNome()+
				" de cpf: "+comandaDelete.getCliente().getLogin()+" deletada com sucesso";
	}
	
	public ComandaModel findComandaByCpf(String cpf, List<Integer> statusId){
		UserDetails clienteDetails  = clienteRepository.findByLogin(cpf);
		if(clienteDetails == null) {
			return null;
		}
		 return comandaRepository.findComandaByCpf(cpf, statusId);
	}
	
	// método para retornar comanda do cliente logado
	public ComandaModel findMyComanda(){
		 // Obtém o contexto de segurança
	    SecurityContext securityContext = SecurityContextHolder.getContext();

	    // Obtém a autenticação do contexto de segurança
	    Authentication authentication = securityContext.getAuthentication();
	    
	 // Verifica se a autenticação é do tipo UsernamePasswordAuthenticationToken
	    if (authentication instanceof UsernamePasswordAuthenticationToken) {
	    	// Obtem detalhes do ClienteModel
	        ClienteModel clienteModel = (ClienteModel) authentication.getPrincipal();
	        // Obtém o CPF do cliente
	        String cpfDoUsuarioAutenticado = clienteModel.getLogin();
	        
	        ComandaModel comandaCliente = comandaRepository.findComandaByCpf(cpfDoUsuarioAutenticado,Arrays.asList(StatusModel.ABERTA));
	        if(comandaCliente == null) {
	        	throw new RuntimeException("Comanda não encontrada para cpf "+cpfDoUsuarioAutenticado);
	        }
	        // hipermidia para buscar os pedidos
	        comandaCliente.add(linkTo(methodOn(PedidoController.class).getPedidosByCpf(cpfDoUsuarioAutenticado)).withRel("pedidos"));
	    
	        return comandaCliente; // retorna comanda o cliente logado

	    }
	    return null;
	}
	
	public void updateValor(Integer id,double valor) {
		ComandaModel comanda = comandaRepository.findById(id).orElseThrow(() -> new RuntimeException("Comanda não encontrado com ID: " + id));
		
		var valorTotal = comanda.getValorTotal() + valor;
		BigDecimal v = BigDecimal.valueOf(valorTotal).setScale(2, RoundingMode.HALF_EVEN);
		valorTotal = v.doubleValue();
		
		comanda.setValorTotal(valorTotal);
		comandaRepository.save(comanda);
	}

}
