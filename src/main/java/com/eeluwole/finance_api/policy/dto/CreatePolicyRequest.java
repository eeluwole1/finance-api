package com.eeluwole.finance_api.policy.dto;

import com.eeluwole.finance_api.policy.Policy.PolicyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreatePolicyRequest {
    @NotNull
    private Long clientId;

    @NotBlank
    private String policyNumber;

    @NotNull
    private PolicyType type;

    @NotNull
    @Positive
    private BigDecimal coverageAmount;

    @NotNull
    @Positive
    private BigDecimal premiumAmount;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
