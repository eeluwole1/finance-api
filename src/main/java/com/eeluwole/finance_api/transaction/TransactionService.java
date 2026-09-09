package com.eeluwole.finance_api.transaction;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientAccessGuard;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.transaction.dto.CreateTransactionRequest;
import com.eeluwole.finance_api.transaction.dto.TransactionResponse;
import com.eeluwole.finance_api.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ClientRepository clientRepository;
    private final ClientAccessGuard clientAccessGuard;

    public TransactionService(TransactionRepository transactionRepository,
            ClientRepository clientRepository,
            ClientAccessGuard clientAccessGuard) {
        this.transactionRepository = transactionRepository;
        this.clientRepository = clientRepository;
        this.clientAccessGuard = clientAccessGuard;
    }

    public List<TransactionResponse> getAllTransactions(User currentUser) {
        return transactionRepository.findAll().stream()
                .filter(t -> clientAccessGuard.owns(t.getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public TransactionResponse getTransactionById(Long id, User currentUser) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        clientAccessGuard.assertOwnership(transaction.getClient(), currentUser);
        return toResponse(transaction);
    }

    public List<TransactionResponse> getTransactionsByClient(Long clientId, User currentUser) {
        clientAccessGuard.requireOwnedClient(clientId, currentUser);
        return transactionRepository.findByClientId(clientId).stream().map(this::toResponse).toList();
    }

    public List<TransactionResponse> getTransactionsByType(Transaction.TransactionType type, User currentUser) {
        return transactionRepository.findByType(type).stream()
                .filter(t -> clientAccessGuard.owns(t.getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public List<TransactionResponse> getTransactionsByStatus(Transaction.TransactionStatus status, User currentUser) {
        return transactionRepository.findByStatus(status).stream()
                .filter(t -> clientAccessGuard.owns(t.getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public TransactionResponse createTransaction(CreateTransactionRequest request, User currentUser) {
        Client client = clientAccessGuard.requireOwnedClient(request.getClientId(), currentUser);

        Transaction transaction = new Transaction();
        transaction.setClient(client);
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());

        // Only set toClient if it's a TRANSFER — the recipient need not be owned by the caller
        if (request.getType() == Transaction.TransactionType.TRANSFER
                && request.getToClientId() != null) {
            Client toClient = clientRepository.findById(request.getToClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Recipient not found with id: " + request.getToClientId()));
            transaction.setToClient(toClient);
        }

        return toResponse(transactionRepository.save(transaction));
    }

    public TransactionResponse updateTransactionStatus(Long id, Transaction.TransactionStatus status, User currentUser) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        clientAccessGuard.assertOwnership(transaction.getClient(), currentUser);
        transaction.setStatus(status);
        return toResponse(transactionRepository.save(transaction));
    }

    public void deleteTransaction(Long id, User currentUser) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        clientAccessGuard.assertOwnership(transaction.getClient(), currentUser);
        transactionRepository.deleteById(id);
    }

    private TransactionResponse toResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setClientId(transaction.getClient().getId());
        response.setClientName(transaction.getClient().getFirstName()
                + " " + transaction.getClient().getLastName());
        response.setType(transaction.getType());
        response.setAmount(transaction.getAmount());
        response.setDescription(transaction.getDescription());
        response.setStatus(transaction.getStatus());
        response.setCreatedAt(transaction.getCreatedAt());

        // toClient is only set for TRANSFER
        if (transaction.getToClient() != null) {
            response.setToClientId(transaction.getToClient().getId());
            response.setToClientName(transaction.getToClient().getFirstName()
                    + " " + transaction.getToClient().getLastName());
        }

        return response;
    }
}
