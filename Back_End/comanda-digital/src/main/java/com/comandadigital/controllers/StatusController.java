package com.comandadigital.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.models.StatusModel;
import com.comandadigital.repositories.StatusRepository;

@RestController
public class StatusController {
	
	@Autowired
	StatusRepository statusRepository;
	
	//listar status
	@GetMapping("/status")
	public ResponseEntity<List<StatusModel>> getAllStatus(){
		List<StatusModel> statusList = statusRepository.findAll();
		
		return ResponseEntity.status(HttpStatus.OK).body(statusList);
	}
}
