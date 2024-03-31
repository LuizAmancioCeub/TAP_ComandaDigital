package com.comandadigital.dtos.myValidations;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DigitsOnlyValidator.class)
@Documented
public @interface DigitsOnly {
    String message() default "Deve conter apenas números";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
