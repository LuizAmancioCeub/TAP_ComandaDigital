package com.comandadigital.dtos;

import java.time.LocalDateTime;

import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.StatusModel;

import jakarta.validation.Valid;

public record ComandaRecordDTO(@Valid StatusModel status,@Valid ClienteModel cliente, java.time.LocalDateTime dtAbertura, LocalDateTime ts_atualizacao) {

}
