package com.comandadigital.dtos.myValidations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ItemUniqueValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemUnique {
	 String message() default "Item já existe no seu cardápio";
	    Class<?>[] groups() default {};
	    Class<? extends Payload>[] payload() default {};
}
