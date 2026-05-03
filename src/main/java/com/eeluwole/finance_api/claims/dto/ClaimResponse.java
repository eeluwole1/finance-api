package com.eeluwole.finance_api.claims.dto;

import com.eeluwole.finance_api.claims.Claim;
import java.time.LocalDateTime;

public class ClaimResponse {

    private Long id;
    private Long clientId;
    private String clientName;
    private Long policyId;
    private String policyNumber;
    private Claim.ClaimType type;
    private Double amount;
    private String description;
    private Claim.ClaimStatus status;
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

    public Claim.ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(Claim.ClaimStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}