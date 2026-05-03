package com.eeluwole.finance_api.client;

import com.eeluwole.finance_api.client.dto.CreateClientRequest;
import com.eeluwole.finance_api.client.dto.ClientResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Get all clients
    public List<ClientResponse> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get client by ID
    public ClientResponse getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return toResponse(client);
    }

    // Get clients by status
    public List<ClientResponse> getClientsByStatus(Client.ClientStatus status) {
        return clientRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Create new client
    public ClientResponse createClient(CreateClientRequest request) {
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Client with this email already exists");
        }
        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());
        return toResponse(clientRepository.save(client));
    }

    // Update client
    public ClientResponse updateClient(Long id, CreateClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());
        return toResponse(clientRepository.save(client));
    }

    // Update client status
    public ClientResponse updateClientStatus(Long id, Client.ClientStatus status) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        client.setStatus(status);
        return toResponse(clientRepository.save(client));
    }

    // Delete client
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found with id: " + id);
        }
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