package com.comandadigital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comandadigital.dtos.FeedbackDTO;
import com.comandadigital.models.FeedbackModel;
import com.comandadigital.models.projection.FeedbackProjection;
import com.comandadigital.services.FeedbackService;

import jakarta.validation.Valid;

@RestController
public class FeedBackController {
	
	@Autowired
	FeedbackService feedbackService;
	
	@PostMapping("/feedback")
	public ResponseEntity register(@RequestBody @Valid FeedbackDTO dto ) throws Exception {
		feedbackService.register(dto);
		return ResponseEntity.ok().body("Avaliação registrada");
	}
	
	@GetMapping("/feedback/{cpf}/{item}")
	public ResponseEntity<FeedbackProjection> getByClienteAndItem(@PathVariable String cpf,@PathVariable Integer item) throws Exception{
		return ResponseEntity.ok().body(feedbackService.getByClienteAndItem(cpf,item));
	}
	
	@PutMapping("/feedback")
		public ResponseEntity<FeedbackProjection> update(@RequestBody @Valid FeedbackDTO dto) throws Exception{
		return ResponseEntity.ok().body(feedbackService.update(dto));
		}
}
