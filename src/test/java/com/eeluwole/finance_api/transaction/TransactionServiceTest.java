package com.eeluwole.finance_api.transaction;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.transaction.dto.CreateTransactionRequest;
import com.eeluwole.finance_api.transaction.dto.TransactionResponse;
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
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Client client;
    private Client toClient;
    private Transaction transaction;
    private CreateTransactionRequest request;

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

        toClient = new Client();
        toClient.setId(2L);
        toClient.setFirstName("Jane");
        toClient.setLastName("Doe");
        toClient.setEmail("jane@email.com");
        toClient.setPhone("647-555-5678");
        toClient.setAddress("456 Oak Ave, Toronto");
        toClient.setStatus(Client.ClientStatus.ACTIVE);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setClient(client);
        transaction.setType(Transaction.TransactionType.DEPOSIT);
        transaction.setAmount(1000.0);
        transaction.setDescription("Monthly deposit");
        transaction.setStatus(Transaction.TransactionStatus.PENDING);

        request = new CreateTransactionRequest();
        request.setClientId(1L);
        request.setType(Transaction.TransactionType.DEPOSIT);
        request.setAmount(1000.0);
        request.setDescription("Monthly deposit");
    }

    @Test
    void getAllTransactions_returnsListOfTransactions() {
        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        List<TransactionResponse> result = transactionService.getAllTransactions();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAmount()).isEqualTo(1000.0);
    }

    @Test
    void getTransactionById_existingId_returnsTransaction() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        TransactionResponse result = transactionService.getTransactionById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAmount()).isEqualTo(1000.0);
    }

    @Test
    void getTransactionById_nonExistingId_throwsException() {
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getTransactionById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Transaction not found with id: 99");
    }

    @Test
    void createTransaction_depositRequest_savesAndReturnsTransaction() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionResponse result = transactionService.createTransaction(request);

        assertThat(result.getAmount()).isEqualTo(1000.0);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransaction_clientNotFound_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.createTransaction(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with id: 1");

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void createTransaction_transferWithRecipient_savesAndReturnsTransaction() {
        Transaction transferTransaction = new Transaction();
        transferTransaction.setId(2L);
        transferTransaction.setClient(client);
        transferTransaction.setToClient(toClient);
        transferTransaction.setType(Transaction.TransactionType.TRANSFER);
        transferTransaction.setAmount(500.0);
        transferTransaction.setDescription("Transfer to Jane");
        transferTransaction.setStatus(Transaction.TransactionStatus.PENDING);

        CreateTransactionRequest transferRequest = new CreateTransactionRequest();
        transferRequest.setClientId(1L);
        transferRequest.setToClientId(2L);
        transferRequest.setType(Transaction.TransactionType.TRANSFER);
        transferRequest.setAmount(500.0);
        transferRequest.setDescription("Transfer to Jane");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.findById(2L)).thenReturn(Optional.of(toClient));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transferTransaction);

        TransactionResponse result = transactionService.createTransaction(transferRequest);

        assertThat(result.getAmount()).isEqualTo(500.0);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransaction_transferRecipientNotFound_throwsException() {
        CreateTransactionRequest transferRequest = new CreateTransactionRequest();
        transferRequest.setClientId(1L);
        transferRequest.setToClientId(99L);
        transferRequest.setType(Transaction.TransactionType.TRANSFER);
        transferRequest.setAmount(500.0);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.createTransaction(transferRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Recipient not found with id: 99");

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void updateTransactionStatus_existingId_updatesStatus() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionResponse result = transactionService.updateTransactionStatus(1L, Transaction.TransactionStatus.COMPLETED);

        assertThat(result).isNotNull();
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void deleteTransaction_existingId_deletesTransaction() {
        when(transactionRepository.existsById(1L)).thenReturn(true);

        transactionService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTransaction_nonExistingId_throwsException() {
        when(transactionRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> transactionService.deleteTransaction(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Transaction not found with id: 99");

        verify(transactionRepository, never()).deleteById(any());
    }
}
