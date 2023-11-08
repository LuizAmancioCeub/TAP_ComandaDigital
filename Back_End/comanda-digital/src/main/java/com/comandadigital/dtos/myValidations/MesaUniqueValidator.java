package com.comandadigital.dtos.myValidations;

import org.springframework.beans.factory.annotation.Autowired;

import com.comandadigital.services.MesaServiceImplements;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MesaUniqueValidator implements ConstraintValidator<MesaUnique, Integer> {
	@Autowired
	private MesaServiceImplements mesaServiceImplements;

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		return !mesaServiceImplements.existsById(value);
	}
	
}
