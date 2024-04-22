package com.comandadigital.dtos;

import com.comandadigital.models.GarcomModel;
import com.comandadigital.models.StatusModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MesaRecordDTO(@NotNull(message = "O número da mesa precisa ser informado") 
							@Max(value = 99999, message = "Limite de caracteres atingido") @Min(value = 1, message = "Número da mesa precisa ser positivo")
							Integer id,
							String qr_code, 
							GarcomModel garcom, 
							@Valid StatusModel status) {

}
