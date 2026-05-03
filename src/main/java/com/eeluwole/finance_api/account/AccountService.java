package com.eeluwole.finance_api.account;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.account.dto.CreateAccountRequest;
import com.eeluwole.finance_api.account.dto.AccountResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public AccountService(AccountRepository accountRepository,
            ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream().map(this::toResponse).toList();
    }

    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        return toResponse(account);
    }

    public List<AccountResponse> getAccountsByClient(Long clientId) {
        return accountRepository.findByClientId(clientId).stream().map(this::toResponse).toList();
    }

    public List<AccountResponse> getAccountsByStatus(Account.AccountStatus status) {
        return accountRepository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    public AccountResponse createAccount(CreateAccountRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + request.getClientId()));

        if (accountRepository.existsByAccountNumber(request.getAccountNumber())) {
            throw new RuntimeException("Account number already exists: " + request.getAccountNumber());
        }

        Account account = new Account();
        account.setClient(client);
        account.setAccountNumber(request.getAccountNumber());
        account.setType(request.getType());
        account.setBalance(request.getBalance() != null ? request.getBalance() : 0.0);

        return toResponse(accountRepository.save(account));
    }

    public AccountResponse deposit(Long id, Double amount) {
        if (amount == null || amount <= 0) {
            throw new RuntimeException("Deposit amount must be greater than zero");
        }

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        if (account.getStatus() == Account.AccountStatus.FROZEN) {
            throw new RuntimeException("Cannot deposit to a frozen account");
        }
        if (account.getStatus() == Account.AccountStatus.CLOSED) {
            throw new RuntimeException("Cannot deposit to a closed account");
        }

        if (account.getBalance() + amount > 1000000) {
            throw new RuntimeException("Deposit would exceed maximum balance limit of 1,000,000");
        }

        account.setBalance(account.getBalance() + amount);
        return toResponse(accountRepository.save(account));
    }

    public AccountResponse withdraw(Long id, Double amount) {
        if (amount == null || amount <= 0) {
            throw new RuntimeException("withdrawal amount must be greater than zero");
        }

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        if (account.getStatus() == Account.AccountStatus.FROZEN) {
            throw new RuntimeException("Cannot withdraw from a frozen account");
        }

        if (account.getStatus() == Account.AccountStatus.CLOSED) {
            throw new RuntimeException("Cannot withdraw from a closed account");
        }

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);
        return toResponse(accountRepository.save(account));
    }

    public AccountResponse updateAccountStatus(Long id, Account.AccountStatus status) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        account.setStatus(status);
        return toResponse(accountRepository.save(account));
    }

    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Account not found with id: " + id);
        }
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