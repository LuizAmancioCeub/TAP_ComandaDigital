package com.comandadigital.models.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackProjection {
	
	private Integer avaliacao;
	
	private String comentario;
	
	private ClienteProjection cliente;
	
    private Integer item;
}
