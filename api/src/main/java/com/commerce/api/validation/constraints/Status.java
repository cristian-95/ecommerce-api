package com.commerce.api.validation.constraints;

import com.commerce.api.validation.StatusValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StatusValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Status {

    String message() default "Status inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}