package com.comandadigital.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "TB01_PERFIL")
public class PerfilModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id// id é auto increment e chave primaria
	@Column(name = "NU_PERFIL", nullable = false, unique = true)
	@Setter(AccessLevel.NONE) // para não modificar o id
	private int id;
	
	@Column(name = "PERFIL")
	private String perfil;

}
