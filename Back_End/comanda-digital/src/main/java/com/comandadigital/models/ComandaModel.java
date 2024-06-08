package com.comandadigital.models;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@Table(name = "TB08_COMANDA")
public class ComandaModel extends RepresentationModel<ComandaModel> implements Serializable {
	private static final long serialVersionUID = 9222447677894252840L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id é auto increment e chave primaria
	@Column(name = "NU_COMANDA", nullable = false, unique = true)
	@Setter(AccessLevel.NONE) // para não modificar o id
	private Integer id;
	
	@Column(name = "VALOR_TOTAL", nullable = false)
	private double valorTotal;
	
	@ManyToOne
	@JoinColumn(name = "NU_STATUS", nullable = false)
	private StatusModel status;
	
	@ManyToOne
	@JoinColumn(name = "NU_CLIENTE", nullable = false)
	private ClienteModel cliente;
	
	@Column(name = "TS_ATUALIZACAO")
	private LocalDateTime ts_atualizacao;
	
	@Column(name = "DT_ABERTURA")
	private LocalDateTime dtAbertura;
	
}
