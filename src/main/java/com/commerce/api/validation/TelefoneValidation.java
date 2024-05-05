package com.commerce.api.validation;

import com.commerce.api.validation.constraints.Telefone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefoneValidation implements ConstraintValidator<Telefone, String> {
    @Override
    public void initialize(Telefone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String telefone = s == null ? "" : s;
        return telefone.matches("^[(]?(\\d{2})[)]?\\s?(\\d{4,5})\\s?-?\\s?(\\d{4})$") || telefone.isBlank();
    }
}
