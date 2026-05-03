package com.eeluwole.finance_api.policy;

import com.eeluwole.finance_api.client.Client;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "policies")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String policyNumber;

    @Enumerated(EnumType.STRING)
    private PolicyType type;

    private Double coverageAmount;
    private Double premiumAmount;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private PolicyStatus status = PolicyStatus.ACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public enum PolicyType {
        LIFE, HEALTH, AUTO, HOME
    }

    public enum PolicyStatus {
        ACTIVE, SUSPENDED, CANCELLED, EXPIRED
    }
}