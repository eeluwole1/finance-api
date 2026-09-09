package com.eeluwole.finance_api.advanced;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.policy.Policy;
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
@Table(name = "policy_loans")
public class Advanced {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(precision = 19, scale = 2)
    private BigDecimal loanAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal interestRate;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.ACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum LoanStatus {
        ACTIVE, REPAID, DEFAULTED
    }
}