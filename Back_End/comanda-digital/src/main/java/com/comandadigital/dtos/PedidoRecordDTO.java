package com.comandadigital.dtos;

import com.comandadigital.models.CozinhaModel;
import com.comandadigital.models.ItemModel;
//import com.comandadigital.models.PedidoItemModel;
import com.comandadigital.models.StatusModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PedidoRecordDTO(  @NotNull @Positive @Min(value = 1, message = "Quantidade mínima: 1") @Max(value = 15, message = "Quantidade máxima: 15") int quantidade,
								@NotNull @Positive(message = "Valor precisa ser positivo") @Max(value = 99999, message = "Valor máximo para um pedido atingido") double valor,
								@Size(max = 35,message = "Limite de caracteres atingido: {max}") String observacao,
								@NotNull CozinhaModel cozinha,
						        @Valid StatusModel status,
						        @Valid ItemModel item
						        //@Size(min = 1, max = 6, message = "A quantidade de itens no pedido deve estar entre {min} e {max}") @Valid Set<PedidoItemModel> itens
						        ){

}
