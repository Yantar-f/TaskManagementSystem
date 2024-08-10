package ru.effectivemobile.taskmanagementsystem.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskmanagementsystem.entity.Account;
import ru.effectivemobile.taskmanagementsystem.exception.AccountNotFoundException;
import ru.effectivemobile.taskmanagementsystem.exception.EmailOccupiedException;
import ru.effectivemobile.taskmanagementsystem.model.AccountCreationData;
import ru.effectivemobile.taskmanagementsystem.repository.AccountRepository;
import ru.effectivemobile.taskmanagementsystem.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Account createAccount(AccountCreationData creationData) {
        if (accountRepository.existsByEmail(creationData.email()))
            throw new EmailOccupiedException(creationData.email());

        String encryptedPassword = passwordEncoder.encode(creationData.password());
        Account newAccount = new Account(creationData.email(), encryptedPassword);

        return accountRepository.save(newAccount);
    }

    @Override
    public Account getAccountById(long id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository
                .findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(email));
    }
}
