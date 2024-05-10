package com.comandadigital.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PedidoRecordUpdateDTO(@NotNull @Positive @Min(value = 1, message = "Quantidade mínima: 1") @Max(value = 15, message = "Quantidade máxima: 15")Integer quantidade, 
									@NotNull @Positive(message = "Valor precisa ser positivo") @Max(value = 99999, message = "Valor máximo para um pedido atingido")Double valor, 
									@Size(max = 35,message = "Limite de caracteres atingido: {max}") String observacao) {

}
