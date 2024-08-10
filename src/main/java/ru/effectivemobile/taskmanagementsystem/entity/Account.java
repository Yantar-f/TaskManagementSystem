package ru.effectivemobile.taskmanagementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(
        name = "accounts",
        uniqueConstraints = @UniqueConstraint(name = "unique_email_constraint", columnNames = "email")
)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "encoded_password")
    private String encodedPassword;

    protected Account() {}

    public Account(String email,
                   String encodedPassword) {
        this.email = email;
        this.encodedPassword = encodedPassword;
    }

    public Account(Long id) {
        this.id = id;
    }

    public static Account reference(Long id) {
        return new Account(id);
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public void setEncodedPassword(@NotNull String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return  Objects.equals(id, account.id) &&
                Objects.equals(email, account.email) &&
                Objects.equals(encodedPassword, account.encodedPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, encodedPassword);
    }
}
