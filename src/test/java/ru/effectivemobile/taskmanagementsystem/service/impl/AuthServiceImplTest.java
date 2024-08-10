package ru.effectivemobile.taskmanagementsystem.service.impl;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.effectivemobile.taskmanagementsystem.entity.Account;
import ru.effectivemobile.taskmanagementsystem.exception.AccountNotFoundException;
import ru.effectivemobile.taskmanagementsystem.exception.InvalidAccountCredentialsException;
import ru.effectivemobile.taskmanagementsystem.model.AccountCredentials;
import ru.effectivemobile.taskmanagementsystem.model.Session;
import ru.effectivemobile.taskmanagementsystem.repository.AccountRepository;
import ru.effectivemobile.taskmanagementsystem.service.AuthService;
import ru.effectivemobile.taskmanagementsystem.util.TokenUtil;

import java.util.Optional;

import static org.instancio.Instancio.create;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {
    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final TokenUtil tokenUtil = mock(TokenUtil.class);
    private final AuthService sut = new AuthServiceImpl(accountRepository, passwordEncoder, tokenUtil);

    @Test
    public void Creating_session_is_successful() {
        var expectedEmail = create(String.class);
        var expectedPass = create(String.class);
        var expectedSession = create(Session.class);
        var expectedCredentials = new AccountCredentials(expectedEmail, expectedPass);
        var expectedAccount = create(Account.class);

        when(passwordEncoder.matches(eq(expectedPass), eq(expectedAccount.getEncodedPassword())))
                .thenReturn(true);

        when(accountRepository.findByEmail(eq(expectedEmail)))
                .thenReturn(Optional.of(expectedAccount));

        when(tokenUtil.generateAccessToken(any()))
                .thenReturn(expectedSession.accessToken());

        var actualSession = sut.createSession(expectedCredentials);

        assertEquals(expectedSession, actualSession);
    }

    @Test
    public void Creating_session_with_invalid_pass_is_failed() {
        var expectedEmail = create(String.class);
        var expectedPass = create(String.class);
        var expectedCredentials = new AccountCredentials(expectedEmail, expectedPass);
        var expectedAccount = create(Account.class);

        when(passwordEncoder.matches(eq(expectedPass), eq(expectedAccount.getEncodedPassword())))
                .thenReturn(false);

        when(accountRepository.findByEmail(eq(expectedEmail)))
                .thenReturn(Optional.of(expectedAccount));

        assertThrows(InvalidAccountCredentialsException.class, () -> sut.createSession(expectedCredentials));
    }

    @Test
    public void Creating_session_with_invalid_email_is_failed() {
        var expectedEmail = create(String.class);
        var credentials = new AccountCredentials(expectedEmail, create(String.class));

        when(accountRepository.findByEmail(eq(expectedEmail)))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () ->  sut.createSession(credentials));
    }
}