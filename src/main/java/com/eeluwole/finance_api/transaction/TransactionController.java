package com.eeluwole.finance_api.transaction;

import com.eeluwole.finance_api.transaction.dto.CreateTransactionRequest;
import com.eeluwole.finance_api.transaction.dto.TransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // GET /api/v1/transactions
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    // GET /api/v1/transactions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    // GET /api/v1/transactions/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(transactionService.getTransactionsByClient(clientId));
    }

    // GET /api/v1/transactions/type/{type}
    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByType(
            @PathVariable Transaction.TransactionType type) {
        return ResponseEntity.ok(transactionService.getTransactionsByType(type));
    }

    // GET /api/v1/transactions/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByStatus(
            @PathVariable Transaction.TransactionStatus status) {
        return ResponseEntity.ok(transactionService.getTransactionsByStatus(status));
    }

    // POST /api/v1/transactions
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody CreateTransactionRequest request) {
        return ResponseEntity.ok(transactionService.createTransaction(request));
    }

    // PATCH /api/v1/transactions/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<TransactionResponse> updateTransactionStatus(
            @PathVariable Long id,
            @RequestParam Transaction.TransactionStatus status) {
        return ResponseEntity.ok(transactionService.updateTransactionStatus(id, status));
    }

    // DELETE /api/v1/transactions/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}