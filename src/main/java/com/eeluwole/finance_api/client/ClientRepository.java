package com.eeluwole.finance_api.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByStatus(Client.ClientStatus status);

    Optional<Client> findByEmail(String email);

    boolean existsByEmail(String email);
}