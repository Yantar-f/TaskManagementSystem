package ru.effectivemobile.taskmanagementsystem.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class NotBlankOptionalStringValidator implements ConstraintValidator<NotBlankOptionalString, Optional<String>> {
    @Override
    public boolean isValid(Optional<String> s, ConstraintValidatorContext constraintValidatorContext) {
        return s.map(str -> str.length() > 0).orElse(true);
    }
}
