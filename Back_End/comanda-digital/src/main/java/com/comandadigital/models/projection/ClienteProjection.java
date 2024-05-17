package com.comandadigital.models.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteProjection {
	private String nome;
	private String telefone;
	private String email;
	private String login;
	private Integer perfil;
}
