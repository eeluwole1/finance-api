package com.eeluwole.finance_api.client;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.common.ResourceNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Central ownership check for every domain that hangs off a Client.
 * ADMIN users bypass ownership entirely; USER users may only touch
 * clients (and anything belonging to them) that they created.
 * A non-owned client is reported as 404, not 403, so a USER can't
 * distinguish "not yours" from "doesn't exist".
 */
@Component
public class ClientAccessGuard {

    private final ClientRepository clientRepository;

    public ClientAccessGuard(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client requireOwnedClient(Long clientId, User currentUser) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));
        assertOwnership(client, currentUser);
        return client;
    }

    public void assertOwnership(Client client, User currentUser) {
        if (!owns(client, currentUser)) {
            throw new ResourceNotFoundException("Client not found with id: " + client.getId());
        }
    }

    public boolean owns(Client client, User currentUser) {
        if (currentUser.getRole() == User.Role.ADMIN) {
            return true;
        }
        return client.getOwnerUserId() != null && client.getOwnerUserId().equals(currentUser.getId());
    }
}
