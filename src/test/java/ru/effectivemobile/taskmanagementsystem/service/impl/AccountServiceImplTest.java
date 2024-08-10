package ru.effectivemobile.taskmanagementsystem.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.effectivemobile.taskmanagementsystem.entity.Account;
import ru.effectivemobile.taskmanagementsystem.exception.AccountNotFoundException;
import ru.effectivemobile.taskmanagementsystem.exception.EmailOccupiedException;
import ru.effectivemobile.taskmanagementsystem.model.AccountCreationData;
import ru.effectivemobile.taskmanagementsystem.repository.AccountRepository;
import ru.effectivemobile.taskmanagementsystem.service.AccountService;

import java.util.Optional;

import static org.instancio.Instancio.create;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {
    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AccountService sut = new AccountServiceImpl(accountRepository, passwordEncoder);

    @Test
    public void Account_creation_is_successful() {
        var expectedEmail = create(String.class);
        var expectedPassword = create(String.class);
        var expectedEncodedPassword = create(String.class);
        var creationData = new AccountCreationData(expectedEmail, expectedPassword);
        var expectedAccount = create(Account.class);

        when(passwordEncoder.encode(eq(expectedPassword)))
                .thenReturn(expectedEncodedPassword);

        when(accountRepository.existsByEmail(eq(expectedEmail)))
                .thenReturn(false);

        when(accountRepository.save(argThat(acc ->
                        acc.getEmail().equals(expectedEmail) &&
                        acc.getEncodedPassword().equals(expectedEncodedPassword))))
                .thenReturn(expectedAccount);

        var actualAccount = sut.createAccount(creationData);

        assertEquals(expectedAccount, actualAccount);

        verify(accountRepository)
                .save(argThat(acc ->
                        acc.getEmail().equals(expectedEmail) &&
                        acc.getEncodedPassword().equals(expectedEncodedPassword)));
    }

    @Test
    public void Account_creation_with_occupied_email_is_failed() {
        var expectedEmail = create(String.class);
        var expectedPassword = create(String.class);
        var creationData = new AccountCreationData(expectedEmail, expectedPassword);

        when(accountRepository.existsByEmail(eq(expectedEmail)))
                .thenReturn(true);

        assertThrows(EmailOccupiedException.class, () -> sut.createAccount(creationData));

        verify(accountRepository, never())
                .save(any());
    }

    @Test
    public void Getting_account_by_id_is_successful() {
        var expectedId = create(Long.class);
        var expectedAccount = create(Account.class);

        when(accountRepository.findById(eq(expectedId)))
                .thenReturn(Optional.of(expectedAccount));

        var actualAccount = sut.getAccountById(expectedId);

        assertEquals(expectedAccount, actualAccount);
    }

    @Test
    public void Getting_not_existing_account_by_id_is_failed() {
        var expectedId = create(Long.class);

        when(accountRepository.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> sut.getAccountById(expectedId));
    }

    @Test
    public void Getting_account_by_email_is_successful() {
        var expectedEmail = create(String.class);
        var expectedAccount = create(Account.class);

        when(accountRepository.findByEmail(eq(expectedEmail)))
                .thenReturn(Optional.of(expectedAccount));

        var actualAccount = sut.getAccountByEmail(expectedEmail);

        assertEquals(expectedAccount, actualAccount);
    }

    @Test
    public void Getting_not_existing_account_by_email_is_failed() {
        var expectedEmail = create(String.class);

        when(accountRepository.findByEmail(eq(expectedEmail)))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> sut.getAccountByEmail(expectedEmail));
    }
}