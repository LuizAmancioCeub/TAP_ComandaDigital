package com.comandadigital.models;

import java.io.Serializable;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	@Column(name = "NU_PEDIDO", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id é auto increment e chave primaria
	@Setter(AccessLevel.NONE) // para não modificar o id
	private Integer id;
	
	 @Column(name = "VALOR", nullable = false)
	    private double valor;

	    @ManyToOne
	    @JoinColumn(name = "NU_STATUS", nullable = false)
	    private StatusModel status;
	    
	    @ManyToOne
	    @JoinColumn(name = "NU_COMANDA", nullable = false)
	    private ComandaModel comanda;

	    @ManyToOne
	    @JoinColumn(name = "NU_COZINHA", nullable = false)
	    private CozinhaModel cozinha;
	    
	    @OneToMany(mappedBy = "id.pedido")
	    private Set<PedidoItemModel> itens;
	    
	    
	    
	    
}
