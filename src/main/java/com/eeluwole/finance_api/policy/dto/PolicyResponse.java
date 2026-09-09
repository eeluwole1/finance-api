package com.eeluwole.finance_api.policy.dto;

import com.eeluwole.finance_api.policy.Policy.PolicyType;
import com.eeluwole.finance_api.policy.Policy.PolicyStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PolicyResponse {
    private Long id;
    private String policyNumber;
    private PolicyType type;
    private BigDecimal coverageAmount;
    private BigDecimal premiumAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private PolicyStatus status;
    private LocalDateTime createdAt;
    private Long clientId;
    private String clientName;
}