package com.eeluwole.finance_api.claims;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.policy.Policy;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private Policy policy;

    @Enumerated(EnumType.STRING)
    private ClaimType type;

    private Double amount;
    private String description;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status = ClaimStatus.SUBMITTED;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum ClaimType {
        MEDICAL, AUTO, HOME, LIFE
    }

    public enum ClaimStatus {
        SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, PAID
    }
}