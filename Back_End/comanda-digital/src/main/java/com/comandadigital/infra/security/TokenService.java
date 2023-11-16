package com.comandadigital.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.CozinhaModel;


@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	 // Criando token para ClienteModel
    public String generateTokenCliente(ClienteModel cliente) {
        return generateToken(cliente.getLogin(), "CLIENTE");
    }

    // Criando token para CozinhaModel
    public String generateTokenCozinha(CozinhaModel cozinha) {
        return generateToken(cozinha.getLogin(), "COZINHA");
    }
	
	// criando token
	public String generateToken(String login, String tipoUsuario) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token = JWT.create()
					.withIssuer("auth-comanda")
					.withSubject(tipoUsuario + login)
					.withExpiresAt(genExpirationDate())
					.sign(algorithm);
			
			return token;
			
		}catch (JWTCreationException e) {
			throw new RuntimeException("Erro na geração do Token ", e); 
		}
	}
	
	// validando token
	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm)
					.withIssuer("auth-comanda")
					.build()
					.verify(token)
					.getSubject();
		}catch (JWTVerificationException e) {
			return "";
		}
		
	}
	
	
	private Instant genExpirationDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")); // tempo de expiração: 2 hrs - BSB
	}
}
