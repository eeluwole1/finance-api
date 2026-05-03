package com.eeluwole.finance_api.payment.dto;

import com.eeluwole.finance_api.payment.Payment;
import java.time.LocalDateTime;

public class PaymentResponse {

    private Long id;
    private Long clientId;
    private String clientName;
    private Long policyId;
    private String policyNumber;
    private Double amount;
    private Payment.PaymentMethod method;
    private Payment.PaymentStatus status;
    private LocalDateTime paidAt;

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

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Payment.PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(Payment.PaymentMethod method) {
        this.method = method;
    }

    public Payment.PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(Payment.PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}