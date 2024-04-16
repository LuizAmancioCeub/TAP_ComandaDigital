package com.comandadigital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.AuthDTO;
import com.comandadigital.dtos.LoginDTO;
import com.comandadigital.infra.security.AuthService;
import com.comandadigital.services.LoginService;

import jakarta.validation.Valid;

@RestController
public class LoginController {
	@Autowired
	LoginService loginService;
	@Autowired
	AuthService authService;
	
	// Login
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid LoginDTO dto) throws Exception {
		String token = loginService.login(dto);
			
		return ResponseEntity.ok(new AuthDTO(token));
	}
	
	//credenciais
	@GetMapping("/myCredenciais")
	public ResponseEntity myCredenciais() {
		// Obtém o contexto de segurança
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    // Obtém a autenticação do contexto de segurança
	    Authentication authentication = securityContext.getAuthentication();
	    UserDetails user =  (UserDetails) authentication.getPrincipal();
	    String login = user.getUsername();
		var usuario = authService.myCredenciais(login,authentication);
		
		if(usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Não foi possível encontrar as credenciais!!");
		}
		return ResponseEntity.ok().body(usuario);
	}
}
