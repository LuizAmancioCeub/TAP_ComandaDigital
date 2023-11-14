package com.comandadigital.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
		UserDetails cliente = clienteRepository.findByCpf(username);
        if (cliente != null) {
            return cliente;
        }

        UserDetails cozinha = cozinhaRepository.findByTipo(username);
        if (cozinha != null) {
            return cozinha;
        }
        
        throw new UsernameNotFoundException("Usuário não encontrado com a identificação: " + username);
	}

}
