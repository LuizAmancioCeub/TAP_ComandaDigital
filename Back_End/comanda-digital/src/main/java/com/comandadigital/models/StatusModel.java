package com.comandadigital.models;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter

@Entity
@Table(name = "TB13_STATUS")
public class StatusModel extends RepresentationModel<CategoriaModel> implements Serializable {
	private static final long serialVersionUID = 1L; // controle de versão
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id é auto increment e chave primaria
	@Column(name = "NU_STATUS", nullable = false, unique = true)
	@Setter(AccessLevel.NONE) // para não modificar o id
	private Integer id;
	
	@Column(name = "STATUS", nullable = false)
	private String status;
}
