package com.eeluwole.finance_api.beneficiary;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByPolicyId(Long policyId);
}