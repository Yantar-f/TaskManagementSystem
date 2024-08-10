package ru.effectivemobile.taskmanagementsystem.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AccountCredentials(
        @Email(message = "Email to valid")
        @NotEmpty(message = "email should be not empty")
        String email,

        @NotEmpty(message = "Password should be not empty")
        String password
) {
}
