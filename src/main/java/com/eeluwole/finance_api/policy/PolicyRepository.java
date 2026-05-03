package com.eeluwole.finance_api.policy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {

    List<Policy> findByClientId(Long clientId);

    List<Policy> findByStatus(Policy.PolicyStatus status);

    boolean existsByPolicyNumber(String policyNumber);
}