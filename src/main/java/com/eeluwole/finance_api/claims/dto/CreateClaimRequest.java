package com.eeluwole.finance_api.claims.dto;

import com.eeluwole.finance_api.claims.Claim;

public class CreateClaimRequest {

    private Long clientId;
    private Long policyId;
    private Claim.ClaimType type;
    private Double amount;
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