package com.eeluwole.finance_api.account;

import com.eeluwole.finance_api.account.dto.CreateAccountRequest;
import com.eeluwole.finance_api.account.dto.AccountResponse;
import com.eeluwole.finance_api.auth.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // GET /api/v1/accounts
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(accountService.getAllAccounts(currentUser));
    }

    // GET /api/v1/accounts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(accountService.getAccountById(id, currentUser));
    }

    // GET /api/v1/accounts/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByClient(@PathVariable Long clientId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(accountService.getAccountsByClient(clientId, currentUser));
    }

    // GET /api/v1/accounts/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AccountResponse>> getAccountsByStatus(@PathVariable Account.AccountStatus status,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(accountService.getAccountsByStatus(status, currentUser));
    }

    // POST /api/v1/accounts
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(accountService.createAccount(request, currentUser));
    }

    // PATCH /api/v1/accounts/{id}/deposit
    @PatchMapping("/{id}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable Long id,
            @RequestParam Double amount,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(accountService.deposit(id, amount, currentUser));
    }

    // PATCH /api/v1/accounts/{id}/withdraw
    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable Long id,
            @RequestParam Double amount,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(accountService.withdraw(id, amount, currentUser));
    }

    // PATCH /api/v1/accounts/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<AccountResponse> updateAccountStatus(
            @PathVariable Long id,
            @RequestParam Account.AccountStatus status,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(accountService.updateAccountStatus(id, status, currentUser));
    }

    // DELETE /api/v1/accounts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        accountService.deleteAccount(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
