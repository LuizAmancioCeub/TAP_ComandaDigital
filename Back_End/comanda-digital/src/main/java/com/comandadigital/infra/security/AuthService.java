package com.comandadigital.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.CozinhaModel;
import com.comandadigital.repositories.ClienteRepository;
import com.comandadigital.repositories.CozinhaRepository;

@Service
public class AuthService implements UserDetailsService {
	@Autowired
	ClienteRepository clienteRepository;
	@Autowired
	CozinhaRepository cozinhaRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails cliente = clienteRepository.findByLogin(username);
        if (cliente != null) {
            return cliente;
        }

        UserDetails cozinha = cozinhaRepository.findByLogin(username);
        if (cozinha != null) {
            return cozinha;
        }
        
        throw new UsernameNotFoundException("Usuário não encontrado com a identificação: " + username);
	}
	
	public UserDetails myCredenciais(String login, Authentication auth) {
		
	    
	    // Verifica se a autenticação é do tipo UsernamePasswordAuthenticationToken
	    if (auth instanceof UsernamePasswordAuthenticationToken) {
	    	
	        ClienteModel cliente = (ClienteModel) clienteRepository.findByLogin(login);
	        if (cliente != null) {
	            return cliente;
	        }
	        CozinhaModel cozinha = (CozinhaModel) cozinhaRepository.findByLogin(login);
	        if (cozinha != null) {
	            return cozinha;
	        }
	    
	    }
	    
	    return null;
	}

}
