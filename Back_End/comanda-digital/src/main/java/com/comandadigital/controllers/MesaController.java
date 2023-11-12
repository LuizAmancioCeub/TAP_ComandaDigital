package com.comandadigital.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.MesaRecordDTO;
import com.comandadigital.models.MesaModel;
import com.comandadigital.services.MesaServiceImplements;

import jakarta.validation.Valid;

@RestController
public class MesaController {
	@Autowired
	MesaServiceImplements mesaServiceImplements;
	
	@PostMapping("/mesa")
	public ResponseEntity<MesaModel> saveMesa(@RequestBody @Valid MesaRecordDTO mesaDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(mesaServiceImplements.register(mesaDto));
	}
	
	@GetMapping("/mesas")
	public ResponseEntity<List<MesaModel>> getAllMesas(){
		List<MesaModel> mesaList = mesaServiceImplements.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(mesaList);
	}
	
	@GetMapping("/mesa/{id}")
	public ResponseEntity<Object> getOneMesaId(@PathVariable(value="id")Integer id) {
		Optional<MesaModel> mesa0 = mesaServiceImplements.findById(id);
		if(mesa0 == null) {
			 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mesa não encontrada!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(mesa0.get());
	}
	
	@PutMapping("/mesa/{id}")
	public ResponseEntity<Object> updateMesa(@PathVariable(value="id") Integer id, @RequestBody @Valid MesaRecordDTO dto){
		MesaModel updateMesa = mesaServiceImplements.update(id, dto);
		
		if(updateMesa == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível alterar o status da mesa!!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(updateMesa);
	}

}
