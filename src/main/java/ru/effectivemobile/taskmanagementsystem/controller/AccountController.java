package ru.effectivemobile.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskmanagementsystem.entity.Account;
import ru.effectivemobile.taskmanagementsystem.model.AccountCreationData;
import ru.effectivemobile.taskmanagementsystem.model.AccountDTO;
import ru.effectivemobile.taskmanagementsystem.service.AccountService;

import java.net.URI;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@Validated
@RestController
@Tag(name = "Account")
@RequestMapping(AccountController.ACCOUNTS_PATH)
public class AccountController {
    public static final String ACCOUNTS_PATH = "/accounts";
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @Operation(
            summary = "create account",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "account created",
                            headers = @Header(
                                    name = LOCATION,
                                    description = "account uri")),
                    @ApiResponse(
                            responseCode = "400",
                            description = "invalid data (constraints violation)",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "some account data already occupied",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)))})

    public ResponseEntity<Void> createAccount(@Valid @RequestBody AccountCreationData creationData) {
        Account newAccount = accountService.createAccount(creationData);
        URI uri = generateUriFor(newAccount);
        return created(uri).build();
    }

    @GetMapping("{id}")
    @Operation(
            summary = "get account by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "account received",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AccountDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "invalid data (constraints violation)",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "account not found",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)))})

    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        AccountDTO dto = new AccountDTO(account);
        return ok(dto);
    }

    @GetMapping
    @Operation(
            summary = "get account by email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "account received",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AccountDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "invalid data (constraints violation)",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "account not found",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)))})

    public ResponseEntity<AccountDTO> getAccountByEmail(
            @NotEmpty(message = "Email should be not empty") @RequestParam("email") String email) {

        Account account = accountService.getAccountByEmail(email);
        AccountDTO dto = new AccountDTO(account);
        return ok(dto);
    }

    private URI generateUriFor(Account newAccount) {
        return URI.create(ACCOUNTS_PATH + "/" + newAccount.getId());
    }
}
