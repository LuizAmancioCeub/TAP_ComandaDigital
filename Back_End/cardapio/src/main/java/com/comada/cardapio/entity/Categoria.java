package com.comada.cardapio.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.AccessLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "TB12_Categoria")

//Lombock
@NoArgsConstructor // construtor sem argumentos

@Getter 
@Setter

public class Categoria {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id é auto increment e chave primaria
	@Column(name = "NU_CATEGORIA", nullable = false, unique = true)
	@Setter(AccessLevel.NONE) // para não modificar o id
	private Long id;
	
	@Column(name = "CATEGORIA", nullable = false)
	private String categoria;

	


	// não é necessário passar o id
	@Builder
	public Categoria(String categoria) {
		this.categoria = categoria;
	}
	

	
	
}
