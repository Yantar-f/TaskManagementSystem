package ru.effectivemobile.taskmanagementsystem.model;

import ru.effectivemobile.taskmanagementsystem.entity.Account;

public record AccountDTO(Long id, String email) {
    public AccountDTO(Account account) {
        this(account.getId(), account.getEmail());
    }
}
