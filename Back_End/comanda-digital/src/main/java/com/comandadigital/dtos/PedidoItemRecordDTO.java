//package com.comandadigital.dtos;
//
//import org.hibernate.validator.constraints.Length;
//import org.hibernate.validator.constraints.Range;
//
//import com.comandadigital.models.PedidoItemPK;
//
//import jakarta.validation.constraints.Positive;
//
//public record PedidoItemRecordDTO( 	PedidoItemPK pedidoItemPK,
//									double valor,
//									@Positive @Range(min = 1, max = 6, message = "A quantidade deve estar entre {min} e {max}") int quantidade,
//							        @Length(max = 35, message = "O campo observação deve ter até {max} caracteres") String observacao,
//									Integer statusId) {
//
//}
