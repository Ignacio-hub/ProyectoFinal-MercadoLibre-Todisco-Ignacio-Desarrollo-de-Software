package com.mercadolibre.examen_mercadolibre.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidDnaSequenceValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDnaSequence {

    String message() default "La secuencia de ADN no es válida: debe ser una matriz NxN (mínimo 4x4) y contener solo A, T, C, G.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
