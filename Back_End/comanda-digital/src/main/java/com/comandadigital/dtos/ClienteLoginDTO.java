package com.comandadigital.dtos;

import com.comandadigital.models.MesaModel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ClienteLoginDTO(String login, String senha, @NotNull(message = "Mesa precisa ser preenchida") @NotEmpty(message = "Mesa precisa ser preenchida") MesaModel mesa) {

}
