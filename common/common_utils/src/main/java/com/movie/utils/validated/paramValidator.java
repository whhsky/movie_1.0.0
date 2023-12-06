package com.movie.utils.validated;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class paramValidator implements ConstraintValidator<paramValidated, Integer> {
    @Override
    public boolean isValid(Integer s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
