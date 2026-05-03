package com.eeluwole.finance_api.claims;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByClientId(Long clientId);

    List<Claim> findByPolicyId(Long policyId);

    List<Claim> findByStatus(Claim.ClaimStatus status);
}