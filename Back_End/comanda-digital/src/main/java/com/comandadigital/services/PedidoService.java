package com.comandadigital.services;

import java.util.List;

import com.comandadigital.dtos.PedidoRecordDTO;
import com.comandadigital.dtos.PedidoRecordUpdateDTO;
//import com.comandadigital.models.PedidoItemModel;
import com.comandadigital.models.PedidoModel;

public interface PedidoService {
	
	PedidoModel register(PedidoRecordDTO pedidoDTO);
	
	List<PedidoModel> findAll();
	
	//List<PedidoModel> findPedidoByCpf(String cpf);
	
	String delete(Integer id);

	PedidoModel update(Integer id, PedidoRecordUpdateDTO DTO);
	
	
}
