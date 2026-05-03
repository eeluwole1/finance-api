package com.eeluwole.finance_api.advanced;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.policy.Policy;
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
@Table(name = "policy_loans")
public class Advanced {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private Policy policy;

    private Double loanAmount;
    private Double interestRate;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.ACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum LoanStatus {
        ACTIVE, REPAID, DEFAULTED
    }
}