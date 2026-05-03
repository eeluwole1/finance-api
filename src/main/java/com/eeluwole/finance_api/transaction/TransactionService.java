package com.eeluwole.finance_api.transaction;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.transaction.dto.CreateTransactionRequest;
import com.eeluwole.finance_api.transaction.dto.TransactionResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ClientRepository clientRepository;

    public TransactionService(TransactionRepository transactionRepository,
            ClientRepository clientRepository) {
        this.transactionRepository = transactionRepository;
        this.clientRepository = clientRepository;
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream().map(this::toResponse).toList();
    }

    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        return toResponse(transaction);
    }

    public List<TransactionResponse> getTransactionsByClient(Long clientId) {
        return transactionRepository.findByClientId(clientId).stream().map(this::toResponse).toList();
    }

    public List<TransactionResponse> getTransactionsByType(Transaction.TransactionType type) {
        return transactionRepository.findByType(type).stream().map(this::toResponse).toList();
    }

    public List<TransactionResponse> getTransactionsByStatus(Transaction.TransactionStatus status) {
        return transactionRepository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + request.getClientId()));

        Transaction transaction = new Transaction();
        transaction.setClient(client);
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());

        // Only set toClient if it's a TRANSFER
        if (request.getType() == Transaction.TransactionType.TRANSFER
                && request.getToClientId() != null) {
            Client toClient = clientRepository.findById(request.getToClientId())
                    .orElseThrow(() -> new RuntimeException("Recipient not found with id: " + request.getToClientId()));
            transaction.setToClient(toClient);
        }

        return toResponse(transactionRepository.save(transaction));
    }

    public TransactionResponse updateTransactionStatus(Long id, Transaction.TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        transaction.setStatus(status);
        return toResponse(transactionRepository.save(transaction));
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found with id: " + id);
        }
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