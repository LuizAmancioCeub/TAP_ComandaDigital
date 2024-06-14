package com.comandadigital.models.projection;

import com.comandadigital.models.StatusModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PedidosProjection {
	
	private Integer idPedido;
	
	private Integer idItem;
	private String nomeItem;
	private double precoItem;
	
	private int quantidade;
	private double valor;
	private String observacao;
	private String horarioPedido;
	private String horarioEntrega;
	
	private StatusModel status; 
	
	private Integer comanda;
	private String garcom;
	
	private String imagem;
	
	
}
