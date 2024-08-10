package ru.effectivemobile.taskmanagementsystem.service;

import ru.effectivemobile.taskmanagementsystem.entity.Account;
import ru.effectivemobile.taskmanagementsystem.model.AccountCreationData;

public interface AccountService {
    Account createAccount       (AccountCreationData creationData);
    Account getAccountById      (long id);
    Account getAccountByEmail   (String email);
}
