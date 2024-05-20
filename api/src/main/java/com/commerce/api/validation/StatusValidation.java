package com.commerce.api.validation;

import com.commerce.api.validation.constraints.Status;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class StatusValidation implements ConstraintValidator<Status, String> {

    @Override
    public void initialize(Status constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String status = s == null ? "" : s;
        return status.matches("pendente|em_progresso|concluido|cancelado");
    }
}
