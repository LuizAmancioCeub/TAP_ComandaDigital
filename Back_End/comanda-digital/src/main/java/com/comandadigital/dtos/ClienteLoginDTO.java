package com.comandadigital.dtos;

import com.comandadigital.models.MesaModel;

import jakarta.validation.constraints.NotNull;

public record ClienteLoginDTO(String cpf, String senha, @NotNull(message = "Necessário informar uma mesa") MesaModel mesa) {

}
