package com.eeluwole.finance_api.account.dto;

import com.eeluwole.finance_api.account.Account;

public class CreateAccountRequest {

    private Long clientId;
    private String accountNumber;
    private Account.AccountType type;
    private Double balance;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Account.AccountType getType() {
        return type;
    }

    public void setType(Account.AccountType type) {
        this.type = type;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}