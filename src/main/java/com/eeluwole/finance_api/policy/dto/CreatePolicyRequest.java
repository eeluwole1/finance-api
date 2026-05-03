package com.eeluwole.finance_api.policy.dto;

import com.eeluwole.finance_api.policy.Policy.PolicyType;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreatePolicyRequest {
    private Long clientId;
    private String policyNumber;
    private PolicyType type;
    private Double coverageAmount;
    private Double premiumAmount;
    private LocalDate startDate;
    private LocalDate endDate;
}