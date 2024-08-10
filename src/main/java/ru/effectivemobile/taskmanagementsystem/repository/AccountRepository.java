package ru.effectivemobile.taskmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.effectivemobile.taskmanagementsystem.entity.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
}
