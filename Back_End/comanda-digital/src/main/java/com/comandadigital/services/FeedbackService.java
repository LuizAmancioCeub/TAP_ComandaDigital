package com.comandadigital.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comandadigital.dtos.FeedbackDTO;
import com.comandadigital.dtos.myValidations.Exceptions.NegocioException;
import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.FeedbackModel;
import com.comandadigital.models.ItemModel;
import com.comandadigital.models.projection.ClienteProjection;
import com.comandadigital.models.projection.FeedbackProjection;
import com.comandadigital.repositories.ClienteRepository;
import com.comandadigital.repositories.FeedbackRepository;
import com.comandadigital.repositories.ItemRepository;

@Service
public class FeedbackService {
	
	@Autowired
	FeedbackRepository feedbackRepository;
	@Autowired
	ClienteRepository clienteRepository;
	@Autowired
	ItemRepository itemRepository;
	
	@Transactional
	public FeedbackModel register(FeedbackDTO dto) throws Exception {
		try {

			 // Obtém o contexto de segurança
		     SecurityContext securityContext = SecurityContextHolder.getContext();
		    // Obtém a autenticação do contexto de segurança
		    Authentication authentication = securityContext.getAuthentication();
		    
		    if (authentication instanceof UsernamePasswordAuthenticationToken) {
		    	FeedbackModel feedback = new FeedbackModel();
		    	// Obtem detalhes do ClienteModel
		        ClienteModel clienteModel = (ClienteModel) authentication.getPrincipal();
		        // Obtém o CPF do cliente
		        String cpfDoUsuarioAutenticado = clienteModel.getLogin();
		        if(clienteRepository.findByLogin(cpfDoUsuarioAutenticado) == null) {
		        	throw new NegocioException("Cliente não encontrado");
		        }
		        Optional<ItemModel> existingItem = itemRepository.findById(dto.nuItem());	
				if(existingItem == null) {
					throw new NegocioException("Item não encontrado");
				}
				if(feedbackRepository.findByClienteAndItem(clienteModel, existingItem.get()) != null) {
					throw new NegocioException("Já existe avaliação");
				}
				
				feedback.setAvaliacao(dto.avaliacao());
				feedback.setComentario(dto.comentario() != null || dto.comentario().trim() != "" ? dto.comentario() : null);
				feedback.setCliente(clienteModel);
				feedback.setItem(existingItem.get());
				
				return feedbackRepository.save(feedback);
		    }  	
				
			return null;
		}catch (NegocioException e) {
			throw new NegocioException(e.getMessage());
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	@Transactional
	public FeedbackProjection update(FeedbackDTO dto) throws Exception {
		try {
			 // Obtém o contexto de segurança
		     SecurityContext securityContext = SecurityContextHolder.getContext();
		    // Obtém a autenticação do contexto de segurança
		    Authentication authentication = securityContext.getAuthentication();
		    
		    if (authentication instanceof UsernamePasswordAuthenticationToken) {
		    	
		    	FeedbackModel feedback = new FeedbackModel();
		    	// Obtem detalhes do ClienteModel
		        ClienteModel clienteModel = (ClienteModel) authentication.getPrincipal();
		        // Obtém o CPF do cliente
		        String cpfDoUsuarioAutenticado = clienteModel.getLogin();
		        if(clienteRepository.findByLogin(cpfDoUsuarioAutenticado) == null) {
		        	throw new NegocioException("Cliente não encontrado");
		        }
		        Optional<ItemModel> existingItem = itemRepository.findById(dto.nuItem());	
				if(existingItem == null) {
					throw new NegocioException("Item não encontrado");
				}
				feedback = feedbackRepository.findByClienteAndItem(clienteModel, existingItem.get());
				if(feedback == null) {
					throw new NegocioException("Ainda não existe avaliação");
				}
				feedback.setAvaliacao(dto.avaliacao());
				feedback.setComentario(dto.comentario() != null || dto.comentario().trim() != "" ? dto.comentario() : null);
				feedback.setCliente(clienteModel);
				feedback.setItem(existingItem.get());
				feedbackRepository.save(feedback);
				return converterFeedback(feedback);
		    }  	
				
			return null;
		}catch (NegocioException e) {
			throw new NegocioException(e.getMessage());
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public FeedbackProjection getByClienteAndItem(String cpf, Integer itemId) throws Exception {
		try {
			ClienteModel cliente = (ClienteModel) clienteRepository.findByLogin(cpf);
			if(cliente == null) {
		       throw new NegocioException("Cliente não encontrado");
		    }
			Optional<ItemModel> existingItem = itemRepository.findById(itemId);	
			if(existingItem.isEmpty()) {
				throw new NegocioException("Item não existe na base de dados");
			}
			
			FeedbackModel f = feedbackRepository.findByClienteAndItem(cliente, existingItem.get());
			return converterFeedback(f);
		}catch (NegocioException e) {
			throw new NegocioException(e.getMessage());
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		 
	}
	
	private FeedbackProjection converterFeedback(FeedbackModel f) {
		FeedbackProjection f0 = new FeedbackProjection();
		if(f != null) {
			f0.setAvaliacao(f.getAvaliacao());
			f0.setComentario(f.getComentario());
			f0.setCliente(converterCliente(f.getCliente()));
			f0.setItem(f.getItem().getId());
		}
		return f0;
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
}
