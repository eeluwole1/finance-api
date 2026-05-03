package com.eeluwole.finance_api.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByClientId(Long clientId);

    List<Payment> findByPolicyId(Long policyId);

    List<Payment> findByStatus(Payment.PaymentStatus status);
}