package com.eeluwole.finance_api.payment.dto;

import com.eeluwole.finance_api.payment.Payment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreatePaymentRequest {

    @NotNull
    private Long clientId;

    @NotNull
    private Long policyId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Payment.PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(Payment.PaymentMethod method) {
        this.method = method;
    }
}
