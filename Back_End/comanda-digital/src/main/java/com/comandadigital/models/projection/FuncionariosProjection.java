package com.comandadigital.models.projection;

import com.comandadigital.models.PerfilModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FuncionariosProjection {
	private String nome;
	private String cpf;
	private String telefone;
	private PerfilModel perfil;
	private String matricula;
	private String senha;
	
	public FuncionariosProjection(String nome,PerfilModel perfil) {
		this.nome = nome;
		this.perfil = perfil;
	}

	public FuncionariosProjection(String nome, String cpf, String telefone, PerfilModel perfil, String matricula) {
		super();
		this.nome = nome;
		this.cpf = cpf;
		this.telefone = telefone;
		this.perfil = perfil;
		this.matricula = matricula;
	}
	
}
