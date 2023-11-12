package com.comandadigital.models;

import java.io.Serializable;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "TB09_PEDIDO")
public class PedidoModel extends RepresentationModel<PedidoModel> implements Serializable {
	private static final long serialVersionUID = 3279824532929844035L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id é auto increment e chave primaria
	@Column(name = "NU_PEDIDO", nullable = false, unique = true)
	@Setter(AccessLevel.NONE) // para não modificar o id
	private Integer id;
	
	 @Column(name = "VALOR", nullable = false)
	    private double valor;

	    @Column(name = "QUANTIDADE", nullable = false)
	    private int quantidade;

	    @Column(name = "OBSERVACAO", length = 35)
	    private String observacao;

	    @ManyToOne
	    @JoinColumn(name = "NU_STATUS", nullable = false)
	    private StatusModel status;
	    
	    @ManyToOne
	    @JoinColumn(name = "NU_COMANDA", nullable = false)
	    private ComandaModel comanda;

	    @ManyToOne
	    @JoinColumn(name = "NU_COZINHA", nullable = false)
	    private CozinhaModel cozinha;
	    
	    @ManyToMany
	    @JoinTable(
	        name = "TB10_Pedido_Item",
	        joinColumns = @JoinColumn(name = "NU_PEDIDO"),
	        inverseJoinColumns = @JoinColumn(name = "NU_ITEM")
	    )
	    private Set<ItemModel> itens;
	    
}
