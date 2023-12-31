package com.comandadigital.models;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "TB07_MESA")
public class MesaModel extends RepresentationModel<MesaModel> implements Serializable {
	private static final long serialVersionUID = 1L; // controle de versão
	
	@Id// id é auto increment e chave primaria
	@Column(name = "NU_MESA", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "QR_CODE")
	private String qr_code;
	
	@ManyToOne
	@JoinColumn(name = "NU_GARCOM")
	private GarcomModel garcom;
	
	@ManyToOne
	@JoinColumn(name = "NU_STATUS", nullable = false)
	private StatusModel status;
}
