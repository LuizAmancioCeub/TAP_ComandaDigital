package com.comandadigital.dtos;

import java.util.Set;

import com.comandadigital.models.ComandaModel;
import com.comandadigital.models.CozinhaModel;
import com.comandadigital.models.PedidoItemModel;
import com.comandadigital.models.StatusModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PedidoRecordDTO(  @NotNull double valor,
								@NotNull CozinhaModel cozinha,
						        @Valid StatusModel status,
						        @Size(min = 1, max = 6, message = "A quantidade de itens no pedido deve estar entre {min} e {max}") @Valid Set<PedidoItemModel> itens) {

}
