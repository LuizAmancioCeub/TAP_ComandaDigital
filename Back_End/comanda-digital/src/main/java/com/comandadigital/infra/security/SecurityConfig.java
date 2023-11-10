package com.comandadigital.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	SecurityFilter securityFilter;
	
	// desabilitar autenticação padrão do security
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.POST, "/login/registrar").permitAll() // qlqr um pode se registrar
						.requestMatchers(HttpMethod.POST, "/login").permitAll() // qlqr um pode fazer login
						.requestMatchers(HttpMethod.POST, "/mesa").hasRole("CLIENTE")
						.requestMatchers(HttpMethod.GET, "/mesas").hasRole("CLIENTE")
						//.requestMatchers(HttpMethod.POST, "/categorias").hasRole("GERENTE") // para o endpoint /products so Gerente pode fazer POST
						.anyRequest().authenticated()
				)
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // filtro vai acontecer antes do UserNamePassword...
				.build();
				
	}
	
	
	 @Bean
	    public AuthenticationManager authManager(AuthenticationConfiguration authConfiguration) throws Exception {
	    	return authConfiguration.getAuthenticationManager();
	    }
	    
	    // retornar dados criptografados com hash
	    @Bean
	    public PasswordEncoder senhaEncoder() {
	    	return new BCryptPasswordEncoder();
	    }
}
