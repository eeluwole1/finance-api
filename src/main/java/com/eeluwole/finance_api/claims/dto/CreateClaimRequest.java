package com.eeluwole.finance_api.claims.dto;

import com.eeluwole.finance_api.claims.Claim;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreateClaimRequest {

    @NotNull
    private Long clientId;

    @NotNull
    private Long policyId;

    @NotNull
    private Claim.ClaimType type;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotBlank
    private String description;

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

    public Claim.ClaimType getType() {
        return type;
    }

    public void setType(Claim.ClaimType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
