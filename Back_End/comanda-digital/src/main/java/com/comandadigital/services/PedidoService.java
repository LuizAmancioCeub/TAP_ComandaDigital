package com.comandadigital.services;

import java.util.List;

import com.comandadigital.dtos.PedidoRecordDTO;
import com.comandadigital.models.PedidoModel;

public interface PedidoService {
	
	PedidoModel register(PedidoRecordDTO pedidoDTO);
	
	List<PedidoModel> findAll();
	
	//List<PedidoModel> findPedidoByCpf(String cpf);
	
	PedidoModel update(Integer id, PedidoRecordDTO pedidoDTO);
	
	String delete(Integer id);
	
	
}
