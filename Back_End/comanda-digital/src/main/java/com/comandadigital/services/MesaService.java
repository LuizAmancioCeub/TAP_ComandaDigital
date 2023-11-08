package com.comandadigital.services;

import java.util.List;
import java.util.Optional;

import com.comandadigital.dtos.MesaRecordDTO;
import com.comandadigital.models.MesaModel;

public interface MesaService {
	
	List<MesaModel> findAll();
	
	Optional<MesaModel> findById(Integer id);
	
	//List<MesaModel> findByStatus();
	
	MesaModel register(MesaRecordDTO mesaDTO);
	
	MesaModel update(Integer id, MesaRecordDTO mesaDTO);
	
	String delete(Integer id);
	
	boolean existsById(Integer id);
}
