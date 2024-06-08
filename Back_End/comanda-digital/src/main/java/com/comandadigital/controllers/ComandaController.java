package com.comandadigital.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.ComandaRecordDTO;
import com.comandadigital.models.ComandaModel;
import com.comandadigital.models.StatusModel;
import com.comandadigital.models.projection.ComandaProjection;
import com.comandadigital.services.ComandaServiceImplements;

import jakarta.validation.Valid;

@RestController
public class ComandaController {
	@Autowired
	ComandaServiceImplements comandaService;
	
	// Registrar Comanda
	@PostMapping("/comanda")
	public ResponseEntity<ComandaModel> registerComanda(@RequestBody @Valid ComandaRecordDTO comandaDTO){
		return ResponseEntity.status(HttpStatus.CREATED).body(comandaService.register(comandaDTO));
	}
	
	// lista das comandas
	@GetMapping("/comandas")
	public ResponseEntity<List<ComandaModel>> getAllComandas(){
		List<ComandaModel> comandaList = comandaService.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(comandaList);
	}
	
	// lista das comandas
		@GetMapping("/minhasComandas")
	public ResponseEntity<List<ComandaProjection>> getMyComandas(){
		List<ComandaProjection> comandaList = comandaService.findMyComandas();
		return ResponseEntity.status(HttpStatus.OK).body(comandaList);
	}
	
	@GetMapping("comanda/{id}")
	public ResponseEntity<Object> getOneComandaId(@PathVariable(value="id") Integer id){
		Optional<ComandaModel> comanda0 = comandaService.findById(id);
		if(comanda0 == null) {
			 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comanda não encontrado!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(comanda0.get());
	}
	
	@GetMapping("consultarComanda/{cpf}")
	public ResponseEntity<List<ComandaProjection>> getOneComandaId(@PathVariable(value="cpf") String cpf){
		List<ComandaProjection> comanda0 = comandaService.findComandasByCpf(cpf);
		
		return ResponseEntity.status(HttpStatus.OK).body(comanda0);
	}
	
	@GetMapping("consultarComandaAtiva/{cpf}")
	public ResponseEntity<ComandaProjection> getComandaAtiva(@PathVariable(value="cpf") String cpf){
		ComandaProjection comanda0 = comandaService.findComandaByCpf(cpf, Arrays.asList(StatusModel.ABERTA, StatusModel.AGUARDANDO_PAGAMENTO));
		
		return ResponseEntity.status(HttpStatus.OK).body(comanda0);
	}
	
	@GetMapping("consultarComanda/mesa/{mesa}")
	public ResponseEntity<Object> getComandaByMesa(@PathVariable(value="mesa") Integer mesa) throws Exception{
		List<ComandaProjection> comanda0 = comandaService.findComandaByMesa(mesa);
		
		return ResponseEntity.status(HttpStatus.OK).body(comanda0);
	}
	
	@GetMapping("consultarComanda/numero/{numero}")
	public ResponseEntity<Object> getComandaByNumero(@PathVariable(value="numero") Integer numero){
		ComandaProjection comanda0 = comandaService.findComandaByNumero(numero);
		
		return ResponseEntity.status(HttpStatus.OK).body(comanda0);
	}
	
	// deletar pelo id
	@DeleteMapping("/comanda/{id}")
	public ResponseEntity<Object> deleteComanda(@PathVariable(value="id")Integer id){
		return ResponseEntity.status(HttpStatus.OK).body(comandaService.delete(id));
	}
	
	@GetMapping("/minhaComanda")
	public ResponseEntity<Object> getMyComanda(){
		return ResponseEntity.status(HttpStatus.OK).body(comandaService.findMyComanda());
	}
	
	@PutMapping("/minhaComanda/{newStatus}")
	public ResponseEntity updateStatusComanda(@PathVariable(value="newStatus")Integer newStatus) throws Exception {
		ComandaProjection c = comandaService.findMyComanda();
		comandaService.updateStatus(c.getId(), newStatus);
		ComandaProjection cNew = comandaService.findMyComanda();
		return ResponseEntity.status(HttpStatus.OK).body("Status da comanda atualizado para: '"+ cNew.getStatus()+"'");
	}
			
}
