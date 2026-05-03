package com.eeluwole.finance_api.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByClientId(Long clientId);

    List<Transaction> findByType(Transaction.TransactionType type);

    List<Transaction> findByStatus(Transaction.TransactionStatus status);
}