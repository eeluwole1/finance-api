package com.eeluwole.finance_api.account;

import com.eeluwole.finance_api.account.dto.AccountResponse;
import com.eeluwole.finance_api.account.dto.CreateAccountRequest;
import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private AccountService accountService;

    private Client client;
    private Account account;
    private CreateAccountRequest request;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Smith");
        client.setEmail("john@email.com");
        client.setPhone("647-555-1234");
        client.setAddress("123 Main St, Toronto");
        client.setStatus(Client.ClientStatus.ACTIVE);

        account = new Account();
        account.setId(1L);
        account.setAccountNumber("ACC-001");
        account.setClient(client);
        account.setType(Account.AccountType.SAVINGS);
        account.setBalance(500.0);
        account.setStatus(Account.AccountStatus.ACTIVE);

        request = new CreateAccountRequest();
        request.setClientId(1L);
        request.setAccountNumber("ACC-001");
        request.setType(Account.AccountType.SAVINGS);
        request.setBalance(500.0);
    }

    @Test
    void getAllAccounts_returnsListOfAccounts() {
        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<AccountResponse> result = accountService.getAllAccounts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAccountNumber()).isEqualTo("ACC-001");
    }

    @Test
    void getAccountById_existingId_returnsAccount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        AccountResponse result = accountService.getAccountById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAccountNumber()).isEqualTo("ACC-001");
    }

    @Test
    void getAccountById_nonExistingId_throwsException() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Account not found with id: 99");
    }

    @Test
    void createAccount_validRequest_savesAndReturnsAccount() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(accountRepository.existsByAccountNumber("ACC-001")).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponse result = accountService.createAccount(request);

        assertThat(result.getAccountNumber()).isEqualTo("ACC-001");
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_clientNotFound_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.createAccount(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with id: 1");

        verify(accountRepository, never()).save(any());
    }

    @Test
    void createAccount_duplicateAccountNumber_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(accountRepository.existsByAccountNumber("ACC-001")).thenReturn(true);

        assertThatThrownBy(() -> accountService.createAccount(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Account number already exists: ACC-001");

        verify(accountRepository, never()).save(any());
    }

    @Test
    void deposit_validAmount_updatesBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponse result = accountService.deposit(1L, 100.0);

        assertThat(result).isNotNull();
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void deposit_zeroAmount_throwsException() {
        assertThatThrownBy(() -> accountService.deposit(1L, 0.0))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Deposit amount must be greater than zero");
    }

    @Test
    void deposit_frozenAccount_throwsException() {
        account.setStatus(Account.AccountStatus.FROZEN);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.deposit(1L, 100.0))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cannot deposit to a frozen account");
    }

    @Test
    void deposit_closedAccount_throwsException() {
        account.setStatus(Account.AccountStatus.CLOSED);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.deposit(1L, 100.0))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cannot deposit to a closed account");
    }

    @Test
    void deposit_exceedsMaxBalance_throwsException() {
        account.setBalance(950000.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.deposit(1L, 100000.0))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Deposit would exceed maximum balance limit of 1,000,000");
    }

    @Test
    void withdraw_validAmount_updatesBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponse result = accountService.withdraw(1L, 100.0);

        assertThat(result).isNotNull();
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void withdraw_zeroAmount_throwsException() {
        assertThatThrownBy(() -> accountService.withdraw(1L, 0.0))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("withdrawal amount must be greater than zero");
    }

    @Test
    void withdraw_frozenAccount_throwsException() {
        account.setStatus(Account.AccountStatus.FROZEN);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.withdraw(1L, 100.0))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cannot withdraw from a frozen account");
    }

    @Test
    void withdraw_insufficientBalance_throwsException() {
        account.setBalance(50.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.withdraw(1L, 100.0))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Insufficient balance");
    }

    @Test
    void updateAccountStatus_existingId_updatesStatus() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponse result = accountService.updateAccountStatus(1L, Account.AccountStatus.FROZEN);

        assertThat(result).isNotNull();
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void deleteAccount_existingId_deletesAccount() {
        when(accountRepository.existsById(1L)).thenReturn(true);

        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAccount_nonExistingId_throwsException() {
        when(accountRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> accountService.deleteAccount(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Account not found with id: 99");

        verify(accountRepository, never()).deleteById(any());
    }
}
