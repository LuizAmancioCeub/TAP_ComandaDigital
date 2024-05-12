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
public class UserProjection {
	
	private String login;
	private String nome;
	private String cpf;
	private String telefone;
	private PerfilModel perfil;
	private MesaProjection mesa;
	
}
