package com.comandadigital.dtos;

import com.comandadigital.models.MesaModel;

public record LoginDTO(String login,String senha, MesaModel mesa) {

}
