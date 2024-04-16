package com.comandadigital.services;

import com.comandadigital.dtos.ClienteLoginDTO;
import com.comandadigital.dtos.ClienteRegisterDTO;
import com.comandadigital.models.ClienteModel;

public interface ClienteService {
	
	 String login(ClienteLoginDTO dto) throws Exception;
	 
	 ClienteModel register(ClienteRegisterDTO dto);
}
