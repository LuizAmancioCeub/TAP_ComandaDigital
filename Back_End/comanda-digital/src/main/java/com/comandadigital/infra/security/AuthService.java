package com.comandadigital.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.comandadigital.dtos.ClienteResumeDTO;
import com.comandadigital.models.CaixaModel;
import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.CozinhaModel;
import com.comandadigital.models.GarcomModel;
import com.comandadigital.models.GerenteModel;
import com.comandadigital.models.projection.ClienteProjection;
import com.comandadigital.models.projection.UserProjection;
import com.comandadigital.repositories.CaixaRepository;
import com.comandadigital.repositories.ClienteRepository;
import com.comandadigital.repositories.CozinhaRepository;
import com.comandadigital.repositories.GarcomRepository;
import com.comandadigital.repositories.GerenteRepository;

@Service
public class AuthService implements UserDetailsService {
	@Autowired
	ClienteRepository clienteRepository;
	@Autowired
	CozinhaRepository cozinhaRepository;
	@Autowired
	GerenteRepository gerenteRepository;
	@Autowired
	GarcomRepository garcomRepository;
	@Autowired
	CaixaRepository caixaRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails cliente = clienteRepository.findByLogin(username);
        if (cliente != null) {
            return cliente;
        }

        UserDetails gerente = gerenteRepository.findByLogin(username);
        if (gerente != null) {
        	return gerente;
        }

        UserDetails cozinha = cozinhaRepository.findByLogin(username);
        if (cozinha != null) {
            return cozinha;
        }
        
        UserDetails garcom = garcomRepository.findByLogin(username);
        if (garcom != null) {
        	return garcom;
        }
        
        UserDetails caixa = caixaRepository.findByLogin(username);
        if (caixa != null) {
        	return caixa;
        }
        
        throw new UsernameNotFoundException("Usuário não encontrado com a identificação: " + username);
	}
	
	public UserProjection myCredenciais(String login, Authentication auth) {
		
	    
	    // Verifica se a autenticação é do tipo UsernamePasswordAuthenticationToken
	    if (auth instanceof UsernamePasswordAuthenticationToken) {
	    	
	        ClienteModel cliente = (ClienteModel) clienteRepository.findByLogin(login);
	        if (cliente != null) {
	        	UserProjection user = new UserProjection();
	        	user.setLogin(cliente.getLogin());
	        	user.setCpf(cliente.getLogin());
	        	user.setNome(cliente.getNome());
	        	user.setTelefone(cliente.getTelefone());
	        	user.setPerfil(cliente.getPerfil());
	        	user.setMesa(cliente.getMesa());
	            return user;
	        }
	        
	        GerenteModel gerente = (GerenteModel) gerenteRepository.findByLogin(login);
	        if (gerente != null) {
	        	UserProjection user = new UserProjection();
	        	user.setLogin(gerente.getLogin());
	        	user.setCpf(gerente.getCpf());
	        	user.setNome(gerente.getNome());
	        	user.setTelefone(gerente.getTelefone());
	        	user.setPerfil(gerente.getPerfil());
	            return user;
	        }
	        CozinhaModel cozinha = (CozinhaModel) cozinhaRepository.findByLogin(login);
	        if (cozinha != null) {
	        	UserProjection user = new UserProjection();
	        	user.setLogin(cozinha.getLogin());
	        	user.setCpf("");
	        	user.setNome(cozinha.getLogin());
	        	user.setTelefone("");
	        	user.setPerfil(cozinha.getPerfil());
	            return user;
	        }
	        
	        GarcomModel garcom = (GarcomModel) garcomRepository.findByLogin(login);
	        if (garcom != null) {
	        	UserProjection user = new UserProjection();
	        	user.setLogin(garcom.getLogin());
	        	user.setCpf(garcom.getCpf());
	        	user.setNome(garcom.getNome());
	        	user.setTelefone(garcom.getTelefone());
	        	user.setPerfil(garcom.getPerfil());
	            return user;
	        }
	        CaixaModel caixa = (CaixaModel) caixaRepository.findByLogin(login);
	        if (caixa != null) {
	        	UserProjection user = new UserProjection();
	        	user.setLogin(caixa.getLogin());
	        	user.setCpf("");
	        	user.setNome(caixa.getLogin());
	        	user.setTelefone("");
	        	user.setPerfil(caixa.getPerfil());
	            return user;
	        }
	    }
	    
	    return null;
	}

}
