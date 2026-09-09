package com.eeluwole.finance_api.account;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientAccessGuard;
import com.eeluwole.finance_api.account.dto.CreateAccountRequest;
import com.eeluwole.finance_api.account.dto.AccountResponse;
import com.eeluwole.finance_api.common.AppConstants;
import com.eeluwole.finance_api.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@SuppressWarnings("null")
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientAccessGuard clientAccessGuard;

    public AccountService(AccountRepository accountRepository,
            ClientAccessGuard clientAccessGuard) {
        this.accountRepository = accountRepository;
        this.clientAccessGuard = clientAccessGuard;
    }

    public List<AccountResponse> getAllAccounts(User currentUser) {
        return accountRepository.findAll().stream()
                .filter(a -> clientAccessGuard.owns(a.getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public AccountResponse getAccountById(Long id, User currentUser) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        clientAccessGuard.assertOwnership(account.getClient(), currentUser);
        return toResponse(account);
    }

    public List<AccountResponse> getAccountsByClient(Long clientId, User currentUser) {
        clientAccessGuard.requireOwnedClient(clientId, currentUser);
        return accountRepository.findByClientId(clientId).stream().map(this::toResponse).toList();
    }

    public List<AccountResponse> getAccountsByStatus(Account.AccountStatus status, User currentUser) {
        return accountRepository.findByStatus(status).stream()
                .filter(a -> clientAccessGuard.owns(a.getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public AccountResponse createAccount(CreateAccountRequest request, User currentUser) {
        Client client = clientAccessGuard.requireOwnedClient(request.getClientId(), currentUser);

        if (accountRepository.existsByAccountNumber(request.getAccountNumber())) {
            throw new RuntimeException("Account number already exists: " + request.getAccountNumber());
        }

        Account account = new Account();
        account.setClient(client);
        account.setAccountNumber(request.getAccountNumber());
        account.setType(request.getType());
        account.setBalance(request.getBalance() != null ? request.getBalance() : BigDecimal.ZERO);

        return toResponse(accountRepository.save(account));
    }

    public AccountResponse deposit(Long id, BigDecimal amount, User currentUser) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be greater than zero");
        }

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        clientAccessGuard.assertOwnership(account.getClient(), currentUser);

        if (account.getStatus() == Account.AccountStatus.FROZEN) {
            throw new RuntimeException("Cannot deposit to a frozen account");
        }
        if (account.getStatus() == Account.AccountStatus.CLOSED) {
            throw new RuntimeException("Cannot deposit to a closed account");
        }

        if (account.getBalance().add(amount).compareTo(AppConstants.MAX_ACCOUNT_BALANCE) > 0) {
            throw new RuntimeException(String.format(java.util.Locale.US,
                    "Deposit would exceed maximum balance limit of %,.0f", AppConstants.MAX_ACCOUNT_BALANCE));
        }

        account.setBalance(account.getBalance().add(amount));
        return toResponse(accountRepository.save(account));
    }

    public AccountResponse withdraw(Long id, BigDecimal amount, User currentUser) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("withdrawal amount must be greater than zero");
        }

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        clientAccessGuard.assertOwnership(account.getClient(), currentUser);

        if (account.getStatus() == Account.AccountStatus.FROZEN) {
            throw new RuntimeException("Cannot withdraw from a frozen account");
        }

        if (account.getStatus() == Account.AccountStatus.CLOSED) {
            throw new RuntimeException("Cannot withdraw from a closed account");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        return toResponse(accountRepository.save(account));
    }

    public AccountResponse updateAccountStatus(Long id, Account.AccountStatus status, User currentUser) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        clientAccessGuard.assertOwnership(account.getClient(), currentUser);
        account.setStatus(status);
        return toResponse(accountRepository.save(account));
    }

    public void deleteAccount(Long id, User currentUser) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        clientAccessGuard.assertOwnership(account.getClient(), currentUser);
        accountRepository.deleteById(id);
    }

    private AccountResponse toResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setClientId(account.getClient().getId());
        response.setClientName(account.getClient().getFirstName()
                + " " + account.getClient().getLastName());
        response.setType(account.getType());
        response.setBalance(account.getBalance());
        response.setStatus(account.getStatus());
        response.setCreatedAt(account.getCreatedAt());
        return response;
    }
}
