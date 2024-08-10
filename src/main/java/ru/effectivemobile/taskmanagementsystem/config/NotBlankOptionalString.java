package ru.effectivemobile.taskmanagementsystem.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankOptionalStringValidator.class)
public @interface NotBlankOptionalString {
    String message() default "must be missing or not blank";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
