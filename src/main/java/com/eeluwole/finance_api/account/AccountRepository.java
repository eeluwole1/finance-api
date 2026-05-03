package com.eeluwole.finance_api.account;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByClientId(Long clientId);

    List<Account> findByStatus(Account.AccountStatus status);

    boolean existsByAccountNumber(String accountNumber);
}