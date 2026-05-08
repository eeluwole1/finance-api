package com.eeluwole.finance_api.policy;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.policy.dto.CreatePolicyRequest;
import com.eeluwole.finance_api.policy.dto.PolicyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private PolicyService policyService;

    private Client client;
    private Policy policy;
    private CreatePolicyRequest request;

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

        policy = new Policy();
        policy.setId(1L);
        policy.setPolicyNumber("POL-001");
        policy.setClient(client);
        policy.setType(Policy.PolicyType.LIFE);
        policy.setCoverageAmount(500000.0);
        policy.setPremiumAmount(200.0);
        policy.setStartDate(LocalDate.of(2024, 1, 1));
        policy.setEndDate(LocalDate.of(2025, 1, 1));
        policy.setStatus(Policy.PolicyStatus.ACTIVE);

        request = new CreatePolicyRequest();
        request.setClientId(1L);
        request.setPolicyNumber("POL-001");
        request.setType(Policy.PolicyType.LIFE);
        request.setCoverageAmount(500000.0);
        request.setPremiumAmount(200.0);
        request.setStartDate(LocalDate.of(2024, 1, 1));
        request.setEndDate(LocalDate.of(2025, 1, 1));
    }

    @Test
    void getAllPolicies_returnsListOfPolicies() {
        when(policyRepository.findAll()).thenReturn(List.of(policy));

        List<PolicyResponse> result = policyService.getAllPolicies();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPolicyNumber()).isEqualTo("POL-001");
    }

    @Test
    void getPolicyById_existingId_returnsPolicy() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));

        PolicyResponse result = policyService.getPolicyById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPolicyNumber()).isEqualTo("POL-001");
    }

    @Test
    void getPolicyById_nonExistingId_throwsException() {
        when(policyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> policyService.getPolicyById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Policy not found with id: 99");
    }

    @Test
    void createPolicy_validRequest_savesAndReturnsPolicy() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(policyRepository.existsByPolicyNumber("POL-001")).thenReturn(false);
        when(policyRepository.save(any(Policy.class))).thenReturn(policy);

        PolicyResponse result = policyService.createPolicy(request);

        assertThat(result.getPolicyNumber()).isEqualTo("POL-001");
        verify(policyRepository, times(1)).save(any(Policy.class));
    }

    @Test
    void createPolicy_clientNotFound_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> policyService.createPolicy(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with id: 1");

        verify(policyRepository, never()).save(any());
    }

    @Test
    void createPolicy_duplicatePolicyNumber_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(policyRepository.existsByPolicyNumber("POL-001")).thenReturn(true);

        assertThatThrownBy(() -> policyService.createPolicy(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Policy number already exists: POL-001");

        verify(policyRepository, never()).save(any());
    }

    @Test
    void updatePolicy_existingId_updatesAndReturnsPolicy() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(policyRepository.save(any(Policy.class))).thenReturn(policy);

        PolicyResponse result = policyService.updatePolicy(1L, request);

        assertThat(result.getPolicyNumber()).isEqualTo("POL-001");
        verify(policyRepository, times(1)).save(any(Policy.class));
    }

    @Test
    void updatePolicyStatus_existingId_updatesStatus() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(policyRepository.save(any(Policy.class))).thenReturn(policy);

        PolicyResponse result = policyService.updatePolicyStatus(1L, Policy.PolicyStatus.SUSPENDED);

        assertThat(result).isNotNull();
        verify(policyRepository, times(1)).save(any(Policy.class));
    }

    @Test
    void deletePolicy_existingId_deletesPolicy() {
        when(policyRepository.existsById(1L)).thenReturn(true);

        policyService.deletePolicy(1L);

        verify(policyRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePolicy_nonExistingId_throwsException() {
        when(policyRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> policyService.deletePolicy(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Policy not found with id: 99");

        verify(policyRepository, never()).deleteById(any());
    }
}
