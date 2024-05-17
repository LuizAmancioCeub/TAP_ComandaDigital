package com.comandadigital.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.GerenteRegisterDTO;
import com.comandadigital.dtos.GerenteUpdateDTO;
import com.comandadigital.infra.security.AuthService;
import com.comandadigital.models.PerfilModel;
import com.comandadigital.models.projection.FuncionariosProjection;
import com.comandadigital.services.GerenteService;

import jakarta.validation.Valid;

@RestController
public class GerenteController {
	@Autowired
	GerenteService gerenteService;
	@Autowired
	AuthService authService;
	
	
	// Registrar
		@PostMapping("gerente/registrar")
		public ResponseEntity register(@RequestBody @Valid GerenteRegisterDTO dto) throws Exception {
			var gerente0 = gerenteService.register(dto);
			return ResponseEntity.ok().body("Cadastro do Gerente efetuado, faça o login para acessar os serviços."+ "\nMatrícula: "+gerente0.getLogin());
		}
		
		// lista das categorias
		@GetMapping("gerente/funcionariosPerfil")
		public ResponseEntity<List<PerfilModel>> getAllPerfil() throws Exception{
			List<PerfilModel> perfis = gerenteService.getPerfilFuncionarios();
			return ResponseEntity.status(HttpStatus.OK).body(perfis);
		}
		
		// lista das categorias
		@GetMapping("gerente/funcionarios/{perfilId}")
		public ResponseEntity<List<FuncionariosProjection>> getFuncionariosPerfil(@PathVariable Integer perfilId) throws Exception{
			List<FuncionariosProjection> perfis = gerenteService.getFuncionarios(perfilId);
			return ResponseEntity.status(HttpStatus.OK).body(perfis);
		}
		
		@PutMapping("/gerente")
		public ResponseEntity alterarDados(@RequestBody @Valid GerenteUpdateDTO dto)throws Exception{
			gerenteService.alterarDados(dto);
			return  ResponseEntity.ok().body("Seu perfil foi atualizado");
		}
}
