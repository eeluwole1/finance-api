package com.eeluwole.finance_api.client;

import com.eeluwole.finance_api.client.dto.ClientResponse;
import com.eeluwole.finance_api.client.dto.CreateClientRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client;
    private CreateClientRequest request;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Smith");
        client.setEmail("john@email.com");
        client.setPhone("647-555-1234");
        client.setAddress("123 Main St, Toronto");
        client.setStatus(Client.ClientStatus.ACTIVE);

        request = new CreateClientRequest();
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setEmail("john@email.com");
        request.setPhone("647-555-1234");
        request.setAddress("123 Main St, Toronto");
    }

    @Test
    void getAllClients_returnsListOfClients() {
        when(clientRepository.findAll()).thenReturn(List.of(client));

        List<ClientResponse> result = clientService.getAllClients();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("john@email.com");
    }

    @Test
    void getClientById_existingId_returnsClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientResponse result = clientService.getClientById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
    }

    @Test
    void getClientById_nonExistingId_throwsException() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getClientById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with id: 99");
    }

    @Test
    void createClient_newEmail_savesAndReturnsClient() {
        when(clientRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientResponse result = clientService.createClient(request);

        assertThat(result.getEmail()).isEqualTo("john@email.com");
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void createClient_duplicateEmail_throwsException() {
        when(clientRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> clientService.createClient(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client with this email already exists");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void updateClient_existingId_updatesAndReturnsClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientResponse result = clientService.updateClient(1L, request);

        assertThat(result.getFirstName()).isEqualTo("John");
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void updateClientStatus_existingId_updatesStatus() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientResponse result = clientService.updateClientStatus(1L, Client.ClientStatus.INACTIVE);

        assertThat(result).isNotNull();
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void deleteClient_existingId_deletesClient() {
        when(clientRepository.existsById(1L)).thenReturn(true);

        clientService.deleteClient(1L);

        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteClient_nonExistingId_throwsException() {
        when(clientRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> clientService.deleteClient(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with id: 99");

        verify(clientRepository, never()).deleteById(any());
    }
}
