package com.comandadigital.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.comandadigital.repositories.ClienteRepository;
import com.comandadigital.repositories.CozinhaRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
	
	@Autowired
	TokenService tokenService;
	@Autowired
	ClienteRepository clienteRepository;
	@Autowired
	CozinhaRepository cozinhaRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		var token = this.recoverToken(request);
		if(token != null) {
			var login = tokenService.validateToken(token);
			UserDetails userDetails = null;
			
			// Verifica o tipo de usuário com base no token
            if (login != null) {
                if (login.startsWith("CLIENTE")) {
                    userDetails = clienteRepository.findByCpf(login.substring("CLIENTE".length())); // subtraindo a string que verifica o tipo de usuário
                } else if (login.startsWith("COZINHA")) {
                    userDetails = cozinhaRepository.findByTipo(login.substring("COZINHA".length()));
                }
            }
			
            if (userDetails != null) {
                var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
		}
		
		filterChain.doFilter(request, response); // passando para o proximo filtro SecurityConfig
		
	}
	
	private String recoverToken(HttpServletRequest request) {
		var authHeader = request.getHeader("Authorization");
		if(authHeader == null) {
			return null;
		}
		
		return authHeader.replace("Bearer ","");
	}

}
