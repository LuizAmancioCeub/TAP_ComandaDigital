package com.comandadigital.services;

import java.util.List;
import java.util.Optional;

import com.comandadigital.dtos.ComandaRecordDTO;
import com.comandadigital.models.ComandaModel;

public interface ComandaService {
	
	Optional<ComandaModel> findById(Integer id);
	
	List<ComandaModel> findAll(); // retornar  oq ta registrado no sistema
	
	//List<ComandaModel> findComandaByMesa();
	
	ComandaModel register( ComandaRecordDTO comandaDTO); // registro
	
	ComandaModel update(Integer id, ComandaRecordDTO comandaDTO); // update
	
	String delete(Integer id); // delete
}
