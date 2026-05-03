package com.eeluwole.finance_api.transaction.dto;

import com.eeluwole.finance_api.transaction.Transaction;

public class CreateTransactionRequest {

    private Long clientId;
    private Long toClientId; // optional — only for TRANSFER
    private Transaction.TransactionType type;
    private Double amount;
    private String description;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getToClientId() {
        return toClientId;
    }

    public void setToClientId(Long toClientId) {
        this.toClientId = toClientId;
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
}