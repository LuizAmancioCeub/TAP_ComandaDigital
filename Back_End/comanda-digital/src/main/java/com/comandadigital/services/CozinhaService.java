package com.comandadigital.services;

import com.comandadigital.dtos.CozinhaLoginDTO;
import com.comandadigital.dtos.CozinhaRegisterDTO;
import com.comandadigital.models.CozinhaModel;

public interface CozinhaService {
	
	String login(CozinhaLoginDTO dto) throws Exception;
	
	CozinhaModel register(CozinhaRegisterDTO dto);
}
