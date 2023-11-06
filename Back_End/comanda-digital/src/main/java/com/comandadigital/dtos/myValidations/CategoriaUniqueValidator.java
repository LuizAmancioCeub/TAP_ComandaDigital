package com.comandadigital.dtos.myValidations;

import org.springframework.beans.factory.annotation.Autowired;

import com.comandadigital.services.CategoriaServiceImplements;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoriaUniqueValidator implements ConstraintValidator<CategoriaUnique, String> {
	
    @Autowired
    private CategoriaServiceImplements categoriaServiceImplements; 
	
	 @Override
	    public boolean isValid(String value, ConstraintValidatorContext context) {
	        if (value == null) {
	            return true;
	        }
	        return !categoriaServiceImplements.existsByCategoria(value);
	    }
}
