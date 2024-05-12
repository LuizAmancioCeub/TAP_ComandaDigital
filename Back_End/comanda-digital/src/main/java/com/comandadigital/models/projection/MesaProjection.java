package com.comandadigital.models.projection;

import com.comandadigital.models.StatusModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MesaProjection {
	private Integer numero;
	private String qr_code;
	private String garcom;
	private StatusModel status;
}
