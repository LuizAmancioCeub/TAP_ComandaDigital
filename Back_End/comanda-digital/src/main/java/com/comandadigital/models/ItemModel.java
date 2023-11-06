package com.comandadigital.models;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "TB11_ITEM")
public class ItemModel extends RepresentationModel<CategoriaModel> implements Serializable {
	private static final long serialVersionUID = 1L; // controle de versão
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id é auto increment e chave primaria
	@Column(name = "NU_ITEM", nullable = false, unique = true)
	@Setter(AccessLevel.NONE) // para não modificar o id
	private Integer id;
	
	@Column(name = "NOME", nullable = false, unique = true)
	private String nome;
	
	@Column(name="DESCRICAO", nullable = false)
	private String descricao;
	
	@Column(name = "PRECO", nullable = false)
	private double preco;
	
	@Column(name = "IMAGEM", nullable = false)
	private String imagem;
	
	@ManyToOne
	@JoinColumn(name = "NU_CATEGORIA", nullable = false)
	private CategoriaModel categoria;
	
	@ManyToOne
	@JoinColumn(name = "NU_STATUS", nullable = false)
	private StatusModel status;
}
