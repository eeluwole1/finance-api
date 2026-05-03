package com.eeluwole.finance_api.payment.dto;

import com.eeluwole.finance_api.payment.Payment;

public class CreatePaymentRequest {

    private Long clientId;
    private Long policyId;
    private Double amount;
    private Payment.PaymentMethod method;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
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
}