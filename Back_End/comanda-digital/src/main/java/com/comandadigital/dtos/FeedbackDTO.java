package com.comandadigital.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FeedbackDTO(@NotNull(message = "Necessário pontuar avaliação") @Min(value = 1, message = "Avaliação mínima: 1") @Max(value = 5, message = "Avaliação máxima: 5") Integer avaliacao,
						  @Size(max = 45) String comentario,
						  @NotNull(message = "Necessário existir item") @Min(value = 1) Integer nuItem) {

}
