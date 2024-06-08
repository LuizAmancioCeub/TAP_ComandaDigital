package com.comandadigital.models.projection;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ComandaProjection {
	private Integer id;
	private double valorTotal;
	private String status;
	private ClienteProjection cliente;
	private Integer mesa;
	private String garcom;
	private List<PedidosProjection> pedidos;
	private String dtAbertura;
	
	public ComandaProjection(Integer id,double valorTotal, String status, ClienteProjection cliente, Integer mesa,String garcom,List<PedidosProjection> pedidos, String dataAbertura ) {
		this.id = id;
		this.valorTotal = valorTotal;
		this.status = status;
		this.cliente = cliente;
		this.mesa = mesa;
		this.garcom = garcom;
		this.pedidos = pedidos;
		this.dtAbertura = dataAbertura;
	}
	
}
