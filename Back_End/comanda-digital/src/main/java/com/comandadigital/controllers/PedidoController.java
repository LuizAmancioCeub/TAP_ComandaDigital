package com.comandadigital.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.PedidoRecordDTO;
import com.comandadigital.dtos.PedidoRecordUpdateDTO;
//import com.comandadigital.models.PedidoItemModel;
import com.comandadigital.models.PedidoModel;
import com.comandadigital.services.PedidoServiceImplements;

import jakarta.validation.Valid;

@RestController
public class PedidoController {
	@Autowired
	PedidoServiceImplements pedidoService;
	
	//Registrar
	@PostMapping("/pedido")
	public ResponseEntity<PedidoModel> register(@RequestBody @Valid PedidoRecordDTO dto) {
		var pedido = pedidoService.register(dto);
		if(pedido == null) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
	}
	
	//Listar pedidos
	@GetMapping("/pedidos")
	public ResponseEntity<List<PedidoModel>> getAllPedidos(){
		return ResponseEntity.status(HttpStatus.OK).body(pedidoService.findAll());
	}
	
	//Listar pedidos
		@GetMapping("/pedidosCozinha/{stausId}")
		public ResponseEntity<List<PedidoModel>> getAllPedidos(@PathVariable List<Integer> stausId){
			return ResponseEntity.status(HttpStatus.OK).body(pedidoService.findPedidosByStatus(stausId));
		}
	
	// Listar pedido por cpf do cliente
	@GetMapping("/pedidos/{cpf}")
	 public ResponseEntity<List<PedidoModel>> getPedidosByCpf(@PathVariable String cpf) {
		try {
			List<PedidoModel> seusPedidos = pedidoService.findPedidosByCpf(cpf, Arrays.asList(8));
			return ResponseEntity.status(HttpStatus.OK).body(seusPedidos);
			
		}catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
		
	}
	
	@GetMapping("/meusPedidos")
	public ResponseEntity<List<PedidoModel>> getMyPedidos(){
		return ResponseEntity.status(HttpStatus.OK).body(pedidoService.findMyPedidos());
	}
	
	@GetMapping("meusPedidosEntregues")
	public ResponseEntity<List<PedidoModel>> getMyPedidosEntregues(){
		var pedido = pedidoService.findMyPedidosEntregues();
		if(pedido == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(pedido);
	}
	
	@GetMapping("meusPedidosEmPreparo")
	public ResponseEntity<List<PedidoModel>> getMyPedidosEmPreparo(){
		var pedido = pedidoService.findMyPedidosEmPreparo();
		if(pedido == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(pedido);
	}
	

	@PutMapping("/pedido/{id}/{statusId}")
	public void updateStatus(@PathVariable Integer id, @PathVariable Integer statusId) {
		pedidoService.updateStatus(id,statusId);
	}
	

	@PutMapping("/pedido/{id}")
	public ResponseEntity update(@PathVariable Integer id, @RequestBody @Valid PedidoRecordUpdateDTO dto) {
		var pedido = pedidoService.update(id,dto);
		if(pedido == null) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build(); 
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(pedido); 
	}
}
