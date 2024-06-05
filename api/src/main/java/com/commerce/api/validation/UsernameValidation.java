package com.commerce.api.validation;

import com.commerce.api.validation.constraints.Username;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidation implements ConstraintValidator<Username, String> {
    @Override
    public void initialize(Username constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.matches("^([A-Za-z0-9.\\-_]){4,12}$");
    }
}
