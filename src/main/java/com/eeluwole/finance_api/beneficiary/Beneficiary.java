package com.eeluwole.finance_api.beneficiary;

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
@Table(name = "beneficiaries")
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private Policy policy;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String relationship;
    private Double percentage;

    @Enumerated(EnumType.STRING)
    private BeneficiaryStatus status = BeneficiaryStatus.ACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum BeneficiaryStatus {
        ACTIVE, INACTIVE
    }
}