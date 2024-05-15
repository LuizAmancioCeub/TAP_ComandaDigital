package com.comandadigital.models.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Garcomprojection {
	
	private Integer id;
	private String matricula;
	private String cpf;
	private String nome;
	private String telefone;
	private String email;
}
