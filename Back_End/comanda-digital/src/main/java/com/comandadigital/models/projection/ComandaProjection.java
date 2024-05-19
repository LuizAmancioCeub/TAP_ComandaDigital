package com.comandadigital.models.projection;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComandaProjection {
	private Integer id;
	private double valorTotal;
	private String status;
	private ClienteProjection cliente;
	private Integer mesa;
	private String garcom;
	private List<PedidosProjection> pedidos;
	
	public ComandaProjection(Integer id,double valorTotal, String status, ClienteProjection cliente, Integer mesa,String garcom,List<PedidosProjection> pedidos ) {
		this.id = id;
		this.valorTotal = valorTotal;
		this.status = status;
		this.cliente = cliente;
		this.mesa = mesa;
		this.garcom = garcom;
		this.pedidos = pedidos;
	}
	
}
