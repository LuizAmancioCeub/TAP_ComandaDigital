package com.comandadigital.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.PedidoRecordDTO;
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
		return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.register(dto));
	}
	
	//Listar pedidos
	@GetMapping("/pedidos")
	public ResponseEntity<List<PedidoModel>> getAllPedidos(){
		return ResponseEntity.status(HttpStatus.OK).body(pedidoService.findAll());
	}
	
//	// Listar pedido por cpf do cliente
//	@GetMapping("/pedidos/{cpf}")
//	 public ResponseEntity<List<PedidoModel>> getPedidosByCpf(@PathVariable String cpf) {
//		try {
//			List<PedidoModel> seusPedidos = pedidoService.findPedidoByCpf(cpf);
//			return ResponseEntity.status(HttpStatus.OK).body(seusPedidos);
//			
//		}catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//		
//	}
	
	@GetMapping("/meusPedidos")
	public ResponseEntity<List<PedidoModel>> getMyPedidos(){
		return ResponseEntity.status(HttpStatus.OK).body(pedidoService.findMyPedidos());
	}
}
