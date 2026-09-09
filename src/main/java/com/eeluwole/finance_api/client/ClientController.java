package com.eeluwole.finance_api.client;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.client.dto.CreateClientRequest;
import com.eeluwole.finance_api.client.dto.ClientResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // GET /api/v1/clients
    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(clientService.getAllClients(currentUser));
    }

    // GET /api/v1/clients/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(clientService.getClientById(id, currentUser));
    }

    // GET /api/v1/clients/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ClientResponse>> getClientsByStatus(@PathVariable Client.ClientStatus status,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(clientService.getClientsByStatus(status, currentUser));
    }

    // POST /api/v1/clients
    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody CreateClientRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(clientService.createClient(request, currentUser));
    }

    // PUT /api/v1/clients/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id,
            @RequestBody CreateClientRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(clientService.updateClient(id, request, currentUser));
    }

    // PATCH /api/v1/clients/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<ClientResponse> updateStatus(@PathVariable Long id,
            @RequestParam Client.ClientStatus status,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(clientService.updateClientStatus(id, status, currentUser));
    }

    // DELETE /api/v1/clients/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        clientService.deleteClient(id, currentUser);
        return ResponseEntity.ok("Client " + id + " deleted successfully");
    }
}
