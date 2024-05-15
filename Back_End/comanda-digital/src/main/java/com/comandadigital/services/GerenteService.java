package com.comandadigital.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.comandadigital.dtos.GerenteLoginDTO;
import com.comandadigital.dtos.GerenteRegisterDTO;
import com.comandadigital.dtos.myValidations.Exceptions.NegocioException;
import com.comandadigital.infra.security.TokenService;
import com.comandadigital.models.CaixaModel;
import com.comandadigital.models.CozinhaModel;
import com.comandadigital.models.GarcomModel;
import com.comandadigital.models.GerenteModel;
import com.comandadigital.models.PerfilModel;
import com.comandadigital.models.projection.FuncionariosProjection;
import com.comandadigital.repositories.CaixaRepository;
import com.comandadigital.repositories.CozinhaRepository;
import com.comandadigital.repositories.GarcomRepository;
import com.comandadigital.repositories.GerenteRepository;
import com.comandadigital.repositories.PerfilRepository;

import jakarta.validation.Valid;

@Service
public class GerenteService {
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	TokenService tokenService;
	@Autowired
	private GerenteRepository gerenteRepository;
	@Autowired
	private PerfilRepository perfilRepository;
	@Autowired
	private GarcomRepository garcomRepository;
	@Autowired
	private CozinhaRepository cozinhaRepository;
	@Autowired
	private CaixaRepository caixaRepository;
	
	public String login(@RequestBody @Valid GerenteLoginDTO dto) {
		// Validar se existe login
		if(gerenteRepository.findByLogin(dto.login().toUpperCase()) == null) {
			throw new NegocioException("Usuário não encontrado");
		}
		
		var userNameSenha = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
		var auth = this.authManager.authenticate(userNameSenha);
		
		return tokenService.generateTokenGerente((GerenteModel)auth.getPrincipal());
	}
	
	@Transactional
	public GerenteModel register(@RequestBody @Valid GerenteRegisterDTO dto) throws Exception {
		try {
			if(gerenteRepository.findByCpf(dto.cpf()) != null) {
				throw new NegocioException("Já existe Gerente cadastrado com essas credenciais");
			}
			if(dto.perfil().getId() != PerfilModel.GERENTE) {
				throw new NegocioException("cadastro inválido");
			}
			String matricula = gerarMatricula();
			String encryptedPassword = new BCryptPasswordEncoder().encode(dto.senha());
			PerfilModel perfilGerente = perfilRepository.findById(PerfilModel.GERENTE).orElseThrow(() -> new NegocioException("Perfil não encontrado"));
			
			GerenteModel newGerente = new GerenteModel(dto.nome(), dto.telefone(),dto.email(),matricula, dto.cpf(), encryptedPassword, perfilGerente);
			
			return this.gerenteRepository.save(newGerente);
		}catch (NegocioException e) {
			throw new NegocioException(e.getMessage());
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}
	
	public String gerarMatricula() {
		
	        Long maxMatricula = gerenteRepository.findMaxMatricula();

	        if ( Objects.isNull(maxMatricula) || maxMatricula <= 1000) {
	            maxMatricula = 1001L;
	        } else {
	            maxMatricula++;
	        }
	        String newMatricula = "GE"+maxMatricula;

	        return newMatricula;
	}
	
	public List<PerfilModel> getPerfilFuncionarios() throws Exception{
		try {
			List<PerfilModel>perfil = perfilRepository.findPerfilById(Arrays.asList(PerfilModel.GERENTE, PerfilModel.COZINHA, PerfilModel.CAIXA, PerfilModel.GARCOM));
			return perfil ;
		}catch (NegocioException e) {
			throw new NegocioException(e.getMessage());
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public List<FuncionariosProjection> getFuncionarios(Integer perfilId) throws Exception{
		try {
			List<FuncionariosProjection> funcs = new ArrayList();
			if(perfilId.equals(PerfilModel.GERENTE)) {
				List<GerenteModel>gerente = gerenteRepository.findAll();
				if(!gerente.isEmpty()) {
					for(GerenteModel g : gerente) {
						FuncionariosProjection f = new FuncionariosProjection();
						f.setNome(g.getNome());
						f.setCpf(g.getCpf());
						f.setTelefone(g.getTelefone());
						f.setEmail(g.getEmail());
						f.setMatricula(g.getLogin());
						f.setPerfil(g.getPerfil());
						funcs.add(f);
					}
					return funcs;
				}
			}else if(perfilId.equals(PerfilModel.GARCOM)) {
				List<GarcomModel>garcom = garcomRepository.findAll();
				if(!garcom.isEmpty()) {
					for(GarcomModel g : garcom) {
						FuncionariosProjection f = new FuncionariosProjection();
						f.setNome(g.getNome());
						f.setCpf(g.getCpf());
						f.setTelefone(g.getTelefone());
						f.setEmail(g.getEmail());
						f.setMatricula(g.getLogin());
						f.setPerfil(g.getPerfil());
						funcs.add(f);
					}
					return funcs;
				}
			}else if(perfilId.equals(PerfilModel.COZINHA)) {
				List<CozinhaModel>cozinha = cozinhaRepository.findAll();
				if(!cozinha.isEmpty()) {
					for(CozinhaModel c : cozinha) {
						FuncionariosProjection f = new FuncionariosProjection();
						f.setNome(c.getLogin());
						f.setPerfil(c.getPerfil());
						f.setCpf("");
						f.setTelefone("");
						f.setEmail("");
						f.setMatricula("");
						funcs.add(f);
					}
					return funcs;
				}
			}else if(perfilId.equals(PerfilModel.CAIXA)) {
				List<CaixaModel>caixa = caixaRepository.findAll();
				if(!caixa.isEmpty()) {
					for(CaixaModel c : caixa) {
						FuncionariosProjection f = new FuncionariosProjection();
						f.setNome(c.getLogin());
						f.setPerfil(c.getPerfil());
						f.setCpf("");
						f.setTelefone("");
						f.setEmail("");
						f.setMatricula("");
						funcs.add(f);
					}
					return funcs;
				}
			}
			return funcs ;
		}catch (NegocioException e) {
			throw new NegocioException(e.getMessage());
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
