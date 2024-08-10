package ru.effectivemobile.taskmanagementsystem.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long accountId) {
        super("Account not found: id = " + accountId);
    }

    public AccountNotFoundException(String email) {
        super("Account not found: email = " + email);
    }
}
