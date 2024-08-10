package ru.effectivemobile.taskmanagementsystem.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskmanagementsystem.entity.Account;
import ru.effectivemobile.taskmanagementsystem.exception.AccountNotFoundException;
import ru.effectivemobile.taskmanagementsystem.exception.InvalidAccountCredentialsException;
import ru.effectivemobile.taskmanagementsystem.model.AccountCredentials;
import ru.effectivemobile.taskmanagementsystem.model.Session;
import ru.effectivemobile.taskmanagementsystem.model.TokenBody;
import ru.effectivemobile.taskmanagementsystem.repository.AccountRepository;
import ru.effectivemobile.taskmanagementsystem.service.AuthService;
import ru.effectivemobile.taskmanagementsystem.util.TokenUtil;

@Service
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;

    public AuthServiceImpl(AccountRepository accountRepository,
                           PasswordEncoder passwordEncoder,
                           TokenUtil tokenUtil) {

        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public Session createSession(AccountCredentials credentials) {
        Account account = accountRepository
                .findByEmail(credentials.email())
                .orElseThrow(() -> new AccountNotFoundException(credentials.email()));

        if (! passwordEncoder.matches(credentials.password(), account.getEncodedPassword()))
            throw new InvalidAccountCredentialsException();

        TokenBody tokenBody = new TokenBody(account.getId());
        String accessToken = tokenUtil.generateAccessToken(tokenBody);

        return new Session(accessToken);
    }
}
