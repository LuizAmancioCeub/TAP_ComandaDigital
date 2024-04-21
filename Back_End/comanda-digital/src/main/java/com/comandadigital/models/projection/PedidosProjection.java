package com.comandadigital.models.projection;

import java.time.LocalDateTime;

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
	
	private Integer idItem;
	private String nomeItem;
	private double precoItem;
	
	private int quantidade;
	private double valor;
	private LocalDateTime horarioPedido;
	private StatusModel status; 
	
	
}
