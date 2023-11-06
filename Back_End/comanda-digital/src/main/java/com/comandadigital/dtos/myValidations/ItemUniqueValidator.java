package com.comandadigital.dtos.myValidations;

import org.springframework.beans.factory.annotation.Autowired;

import com.comandadigital.services.ItemServiceImplements;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ItemUniqueValidator implements ConstraintValidator<ItemUnique, String>{
	 	
		@Autowired
	    private ItemServiceImplements itemServiceImplements; 
		
		 @Override
		    public boolean isValid(String value, ConstraintValidatorContext context) {
		        if (value == null) {
		            return true;
		        }
		        return !itemServiceImplements.existsByNome(value);
		    }
}
