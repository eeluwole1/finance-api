package com.eeluwole.finance_api.advanced;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdvancedRepository extends JpaRepository<Advanced, Long> {
    List<Advanced> findByClientId(Long clientId);

    List<Advanced> findByStatus(Advanced.LoanStatus status);
}