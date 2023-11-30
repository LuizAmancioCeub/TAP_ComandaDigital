package com.comandadigital.dtos;

import com.comandadigital.models.CozinhaModel;
import com.comandadigital.models.ItemModel;
//import com.comandadigital.models.PedidoItemModel;
import com.comandadigital.models.StatusModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record PedidoRecordDTO(  @NotNull int quantidade,
								@NotNull double valor,
								String observacao,
								@NotNull CozinhaModel cozinha,
						        @Valid StatusModel status,
						        @Valid ItemModel item
						        //@Size(min = 1, max = 6, message = "A quantidade de itens no pedido deve estar entre {min} e {max}") @Valid Set<PedidoItemModel> itens
						        ){

}
