package com.eeluwole.finance_api.transaction.dto;

import com.eeluwole.finance_api.transaction.Transaction;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private Long clientId;
    private String clientName;
    private Long toClientId;
    private String toClientName;
    private Transaction.TransactionType type;
    private Double amount;
    private String description;
    private Transaction.TransactionStatus status;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getToClientId() {
        return toClientId;
    }

    public void setToClientId(Long toClientId) {
        this.toClientId = toClientId;
    }

    public String getToClientName() {
        return toClientName;
    }

    public void setToClientName(String toClientName) {
        this.toClientName = toClientName;
    }

    public Transaction.TransactionType getType() {
        return type;
    }

    public void setType(Transaction.TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Transaction.TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(Transaction.TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}