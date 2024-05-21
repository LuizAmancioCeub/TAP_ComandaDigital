package com.comandadigital.models;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB14_FEEDBACK")
public class FeedbackModel extends RepresentationModel<FeedbackModel> {
	
	@Id
	@Column(name = "NU_FEEDBACK", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id é auto increment e chave primaria
	@Setter(AccessLevel.NONE) // para não modificar o id
	private Integer id;
	
	@Column(name = "AVALIACAO", nullable = false)
	private Integer avaliacao;
	
	@Column(name = "COMENTARIO", nullable = true)
	private String comentario;
	
	@ManyToOne
	@JoinColumn(name = "NU_CLIENTE")
	private ClienteModel cliente;
	
	@ManyToOne
    @JoinColumn(name = "NU_ITEM")
    private ItemModel item;
}
