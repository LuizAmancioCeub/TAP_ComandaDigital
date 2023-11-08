package com.comandadigital.dtos.myValidations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = MesaUniqueValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MesaUnique {
	String message() default "Essa mesa já existe no seu estabelecimento";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
