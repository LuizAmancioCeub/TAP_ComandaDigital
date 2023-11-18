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
//		return httpSecurity
//				.csrf(csrf -> csrf.disable())
//				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//				.authorizeHttpRequests(authorize -> authorize
//						.requestMatchers(HttpMethod.POST, "/login/registrar").permitAll() // qlqr um pode se registrar
//						.requestMatchers(HttpMethod.POST, "/login").permitAll() // qlqr um pode fazer login
//						.requestMatchers(HttpMethod.POST, "/mesa").permitAll() // apenas Gerente
//						.requestMatchers(HttpMethod.GET, "/mesas").permitAll() // lista de quem pode realizar
//						.requestMatchers(HttpMethod.POST, "/categorias").hasRole("GERENTE") 
//						.requestMatchers(HttpMethod.GET, "/pedidos").hasAnyRole("GARCOM","GERENTE","CAIXA","COZINHA")
//						.requestMatchers(HttpMethod.POST, "/pedido").hasRole("CLIENTE")
//						.requestMatchers(HttpMethod.GET, "/status").permitAll()
//						.anyRequest().authenticated()// qualquer outra requisição precisa estar logado
//				)
//				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // filtro vai acontecer antes do UserNamePassword...
//				.cors(cors -> cors.disable())
//				.build();
				
		return httpSecurity.cors().and().csrf().disable()
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.POST, "/login/registrar").permitAll() // qlqr um pode se registrar
						.requestMatchers(HttpMethod.POST, "/login").permitAll() // qlqr um pode fazer login
						.requestMatchers(HttpMethod.POST, "/mesa").permitAll() // apenas Gerente
						.requestMatchers(HttpMethod.GET, "/mesas").permitAll() // lista de quem pode realizar
						.requestMatchers(HttpMethod.DELETE, "/mesa/*").permitAll()
						.requestMatchers(HttpMethod.POST, "/categorias").hasRole("GERENTE") 
						.requestMatchers(HttpMethod.GET, "/pedidos").hasAnyRole("GARCOM","GERENTE","CAIXA","COZINHA")
						.requestMatchers(HttpMethod.POST, "/pedido").hasRole("CLIENTE")
						.requestMatchers(HttpMethod.GET, "/status").permitAll()
						.anyRequest().authenticated()// qualquer outra requisição precisa estar logado
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
	 
	 
	 
	 
//	 @Bean
//	 public FilterRegistrationBean corsFilter() {
//		 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		 CorsConfiguration config = new CorsConfiguration();
//		 config.setAllowCredentials(true);
//		 config.addAllowedOrigin("http://localhost:4200");
//		 config.setExposedHeaders(Arrays.asList(
//				 HttpHeaders.AUTHORIZATION,
//				 HttpHeaders.CONTENT_TYPE,
//				 HttpHeaders.ACCEPT
//		 ));
//		 config.setAllowedMethods(Arrays.asList(
//				 HttpMethod.GET.name(),
//				 HttpMethod.POST.name(),
//				 HttpMethod.PUT.name(),
//				 HttpMethod.DELETE.name(),
//				 HttpMethod.OPTIONS.name()
//		 ));
//		 config.setMaxAge(3600L); // 30 minutos
//		  source.registerCorsConfiguration("/**", config);
//		 //FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//		 FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//		 
//		 // should be set order to -100 because we need to CorsFilter before SpringSecurityFilter
//	        bean.setOrder(-102);
//	        return bean;
//	 }
}
