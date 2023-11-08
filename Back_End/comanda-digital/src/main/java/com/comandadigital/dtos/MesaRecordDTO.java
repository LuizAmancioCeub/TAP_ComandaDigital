package com.comandadigital.dtos;

import com.comandadigital.dtos.myValidations.MesaUnique;
import com.comandadigital.models.GarcomModel;
import com.comandadigital.models.StatusModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record MesaRecordDTO(@NotNull(message = "O número da mesa precisa ser informado") @MesaUnique Integer id, 
							String qr_code, 
							GarcomModel garcom, 
							@Valid StatusModel status) {

}
