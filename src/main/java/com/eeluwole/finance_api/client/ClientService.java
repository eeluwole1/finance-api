package com.eeluwole.finance_api.client;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.client.dto.CreateClientRequest;
import com.eeluwole.finance_api.client.dto.ClientResponse;
import com.eeluwole.finance_api.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientAccessGuard clientAccessGuard;

    public ClientService(ClientRepository clientRepository, ClientAccessGuard clientAccessGuard) {
        this.clientRepository = clientRepository;
        this.clientAccessGuard = clientAccessGuard;
    }

    // Get all clients (own only, unless ADMIN)
    public List<ClientResponse> getAllClients(User currentUser) {
        return clientRepository.findAll()
                .stream()
                .filter(c -> clientAccessGuard.owns(c, currentUser))
                .map(this::toResponse)
                .toList();
    }

    // Get client by ID
    public ClientResponse getClientById(Long id, User currentUser) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        clientAccessGuard.assertOwnership(client, currentUser);
        return toResponse(client);
    }

    // Get clients by status (own only, unless ADMIN)
    public List<ClientResponse> getClientsByStatus(Client.ClientStatus status, User currentUser) {
        return clientRepository.findByStatus(status)
                .stream()
                .filter(c -> clientAccessGuard.owns(c, currentUser))
                .map(this::toResponse)
                .toList();
    }

    // Create new client — the caller becomes its owner
    public ClientResponse createClient(CreateClientRequest request, User currentUser) {
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Client with this email already exists");
        }
        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());
        client.setOwnerUserId(currentUser.getId());
        return toResponse(clientRepository.save(client));
    }

    // Update client
    public ClientResponse updateClient(Long id, CreateClientRequest request, User currentUser) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        clientAccessGuard.assertOwnership(client, currentUser);
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());
        return toResponse(clientRepository.save(client));
    }

    // Update client status
    public ClientResponse updateClientStatus(Long id, Client.ClientStatus status, User currentUser) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        clientAccessGuard.assertOwnership(client, currentUser);
        client.setStatus(status);
        return toResponse(clientRepository.save(client));
    }

    // Delete client
    public void deleteClient(Long id, User currentUser) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        clientAccessGuard.assertOwnership(client, currentUser);
        clientRepository.deleteById(id);
    }

    // Convert Client entity → ClientResponse DTO
    private ClientResponse toResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setFirstName(client.getFirstName());
        response.setLastName(client.getLastName());
        response.setEmail(client.getEmail());
        response.setPhone(client.getPhone());
        response.setAddress(client.getAddress());
        response.setStatus(client.getStatus());
        response.setCreatedAt(client.getCreatedAt());
        return response;
    }
}
