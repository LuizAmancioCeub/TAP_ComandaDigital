package com.comandadigital.dtos;

import com.comandadigital.models.MesaModel;
import com.comandadigital.models.PerfilModel;

public record ClienteResumeDTO(String login, String nome, String telefone, PerfilModel perfil, MesaModel mesa) {

}
