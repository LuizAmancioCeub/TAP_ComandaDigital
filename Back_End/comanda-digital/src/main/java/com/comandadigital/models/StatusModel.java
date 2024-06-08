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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
	
	public static final Integer ATIVO = 1;
	public static final Integer INATIVO = 2;
	public static final Integer EM_PREPARACAO = 3;
	public static final Integer PRONTO_ENTREGA = 4;
	public static final Integer ENTREGUE = 5;
	public static final Integer CANCELADO = 6;
	public static final Integer EM_PROCESSAMENTO = 7;
	public static final Integer ABERTA = 8;
	public static final Integer AGUARDANDO_PAGAMENTO = 9;
	public static final Integer PAGA = 10;
	public static final Integer LIVRE = 11;
	public static final Integer OCUPADA = 12;
	public static final Integer RESERVADA = 13;
	public static final Integer INDISPONIVEL = 14;
}
