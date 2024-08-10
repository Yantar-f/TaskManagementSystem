package ru.effectivemobile.taskmanagementsystem.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AccountCreationData(
        @Email(message = "Email to valid")
        @NotEmpty(message = "email should be not empty")
        String email,

        @NotEmpty(message = "Password should be not empty")
        String password
) {}
