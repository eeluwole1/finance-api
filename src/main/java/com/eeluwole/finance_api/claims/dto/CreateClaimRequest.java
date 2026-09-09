package com.eeluwole.finance_api.claims.dto;

import com.eeluwole.finance_api.claims.Claim;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateClaimRequest {

    @NotNull
    private Long clientId;

    @NotNull
    private Long policyId;

    @NotNull
    private Claim.ClaimType type;

    @NotNull
    @Positive
    private Double amount;

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
