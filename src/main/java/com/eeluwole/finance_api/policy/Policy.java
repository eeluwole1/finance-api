package com.eeluwole.finance_api.policy;

import com.eeluwole.finance_api.client.Client;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
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
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(unique = true)
    private String policyNumber;

    @Enumerated(EnumType.STRING)
    private PolicyType type;

    @Column(precision = 19, scale = 2)
    private BigDecimal coverageAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal premiumAmount;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private PolicyStatus status = PolicyStatus.ACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public enum PolicyType {
        LIFE, HEALTH, AUTO, HOME
    }

    public enum PolicyStatus {
        ACTIVE, SUSPENDED, CANCELLED, EXPIRED
    }
}