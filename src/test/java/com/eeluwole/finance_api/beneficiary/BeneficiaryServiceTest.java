package com.eeluwole.finance_api.beneficiary;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.beneficiary.dto.BeneficiaryResponse;
import com.eeluwole.finance_api.beneficiary.dto.CreateBeneficiaryRequest;
import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientAccessGuard;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.policy.Policy;
import com.eeluwole.finance_api.policy.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class BeneficiaryServiceTest {

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private ClientRepository clientRepository;

    private BeneficiaryService beneficiaryService;

    private Client client;
    private Policy policy;
    private Beneficiary beneficiary;
    private CreateBeneficiaryRequest request;
    private User currentUser;

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

        beneficiary = new Beneficiary();
        beneficiary.setId(1L);
        beneficiary.setPolicy(policy);
        beneficiary.setFirstName("Jane");
        beneficiary.setLastName("Smith");
        beneficiary.setEmail("jane@email.com");
        beneficiary.setPhone("647-555-5678");
        beneficiary.setRelationship("Spouse");
        beneficiary.setPercentage(100.0);
        beneficiary.setStatus(Beneficiary.BeneficiaryStatus.ACTIVE);

        request = new CreateBeneficiaryRequest();
        request.setPolicyId(1L);
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setEmail("jane@email.com");
        request.setPhone("647-555-5678");
        request.setRelationship("Spouse");
        request.setPercentage(100.0);

        currentUser = new User();
        currentUser.setId(99L);
        currentUser.setRole(User.Role.ADMIN);

        beneficiaryService = new BeneficiaryService(beneficiaryRepository, policyRepository, new ClientAccessGuard(clientRepository));
    }

    @Test
    void getAllBeneficiaries_returnsListOfBeneficiaries() {
        when(beneficiaryRepository.findAll()).thenReturn(List.of(beneficiary));

        List<BeneficiaryResponse> result = beneficiaryService.getAllBeneficiaries(currentUser);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("jane@email.com");
    }

    @Test
    void getBeneficiaryById_existingId_returnsBeneficiary() {
        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.of(beneficiary));

        BeneficiaryResponse result = beneficiaryService.getBeneficiaryById(1L, currentUser);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Jane");
    }

    @Test
    void getBeneficiaryById_nonExistingId_throwsException() {
        when(beneficiaryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> beneficiaryService.getBeneficiaryById(99L, currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Beneficiary not found with id: 99");
    }

    @Test
    void createBeneficiary_validRequest_savesAndReturnsBeneficiary() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(beneficiary);

        BeneficiaryResponse result = beneficiaryService.createBeneficiary(request, currentUser);

        assertThat(result.getEmail()).isEqualTo("jane@email.com");
        verify(beneficiaryRepository, times(1)).save(any(Beneficiary.class));
    }

    @Test
    void createBeneficiary_policyNotFound_throwsException() {
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> beneficiaryService.createBeneficiary(request, currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Policy not found with id: 1");

        verify(beneficiaryRepository, never()).save(any());
    }

    @Test
    void updateBeneficiary_existingId_updatesAndReturnsBeneficiary() {
        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.of(beneficiary));
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(beneficiary);

        BeneficiaryResponse result = beneficiaryService.updateBeneficiary(1L, request, currentUser);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        verify(beneficiaryRepository, times(1)).save(any(Beneficiary.class));
    }

    @Test
    void updateBeneficiaryStatus_existingId_updatesStatus() {
        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.of(beneficiary));
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(beneficiary);

        BeneficiaryResponse result = beneficiaryService.updateBeneficiaryStatus(1L, Beneficiary.BeneficiaryStatus.INACTIVE, currentUser);

        assertThat(result).isNotNull();
        verify(beneficiaryRepository, times(1)).save(any(Beneficiary.class));
    }

    @Test
    void deleteBeneficiary_existingId_deletesBeneficiary() {
        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.of(beneficiary));

        beneficiaryService.deleteBeneficiary(1L, currentUser);

        verify(beneficiaryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBeneficiary_nonExistingId_throwsException() {
        when(beneficiaryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> beneficiaryService.deleteBeneficiary(99L, currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Beneficiary not found with id: 99");

        verify(beneficiaryRepository, never()).deleteById(any());
    }
}
