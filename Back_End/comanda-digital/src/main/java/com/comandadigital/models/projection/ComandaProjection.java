package com.comandadigital.models.projection;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComandaProjection {
	private double valorTotal;
	private String status;
	private ClienteProjection cliente;
	private Integer mesa;
	private List<PedidosProjection> pedidos;
	
	public ComandaProjection(double valorTotal, String status, ClienteProjection cliente, Integer mesa,List<PedidosProjection> pedidos ) {
		this.valorTotal = valorTotal;
		this.status = status;
		this.cliente = cliente;
		this.mesa = mesa;
		this.pedidos = pedidos;
	}
	
}
