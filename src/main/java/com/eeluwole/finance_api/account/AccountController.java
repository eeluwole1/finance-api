package com.eeluwole.finance_api.account;

import com.eeluwole.finance_api.account.dto.CreateAccountRequest;
import com.eeluwole.finance_api.account.dto.AccountResponse;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // GET /api/v1/accounts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    // GET /api/v1/accounts/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(accountService.getAccountsByClient(clientId));
    }

    // GET /api/v1/accounts/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AccountResponse>> getAccountsByStatus(@PathVariable Account.AccountStatus status) {
        return ResponseEntity.ok(accountService.getAccountsByStatus(status));
    }

    // POST /api/v1/accounts
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    // PATCH /api/v1/accounts/{id}/deposit
    @PatchMapping("/{id}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable Long id,
            @RequestParam Double amount) {
        return ResponseEntity.ok(accountService.deposit(id, amount));
    }

    // PATCH /api/v1/accounts/{id}/withdraw
    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable Long id,
            @RequestParam Double amount) {
        return ResponseEntity.ok(accountService.withdraw(id, amount));
    }

    // PATCH /api/v1/accounts/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<AccountResponse> updateAccountStatus(
            @PathVariable Long id,
            @RequestParam Account.AccountStatus status) {
        return ResponseEntity.ok(accountService.updateAccountStatus(id, status));
    }

    // DELETE /api/v1/accounts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}