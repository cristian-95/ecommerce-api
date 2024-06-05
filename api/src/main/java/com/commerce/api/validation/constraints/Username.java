package com.commerce.api.validation.constraints;

import com.commerce.api.validation.UsernameValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {

    String message() default "Username inválido.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
