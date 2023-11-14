package com.comandadigital.dtos;

import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.comandadigital.models.ItemModel;
import com.comandadigital.models.StatusModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

public record PedidoRecordDTO(  @Range(min = 1, max = 6, message = "A quantidade deve estar entre {min} e {max}") int quantidade,
						        @Length(max = 35, message = "O campo observação deve ter até {max} caracteres") String observacao,
						        @Valid StatusModel status,
						        @Size(min = 1, max = 6, message = "A quantidade de itens no pedido deve estar entre {min} e {max}") @Valid Set<ItemModel> itens) {

}
