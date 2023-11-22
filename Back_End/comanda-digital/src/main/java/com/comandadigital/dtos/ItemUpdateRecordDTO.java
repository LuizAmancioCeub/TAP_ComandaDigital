package com.comandadigital.dtos;

import com.comandadigital.models.CategoriaModel;
import com.comandadigital.models.StatusModel;

public record ItemUpdateRecordDTO(
		 String descricao, 
		 double preco, 
		 String imagem, 
		CategoriaModel categoria, 
		 StatusModel status) {

}
