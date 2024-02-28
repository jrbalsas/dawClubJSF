package com.daw.club.model.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = DniValidator.class)
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface Dni {

    String message() default "DNI incorrecto"; //Default annotation attribute value

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}