package com.eeluwole.finance_api.claims;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.claims.dto.ClaimResponse;
import com.eeluwole.finance_api.claims.dto.CreateClaimRequest;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PolicyRepository policyRepository;

    private ClaimService claimService;

    private Client client;
    private Policy policy;
    private Claim claim;
    private CreateClaimRequest request;
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
        policy.setCoverageAmount(BigDecimal.valueOf(500000.0));
        policy.setPremiumAmount(BigDecimal.valueOf(200.0));
        policy.setStartDate(LocalDate.of(2024, 1, 1));
        policy.setEndDate(LocalDate.of(2025, 1, 1));
        policy.setStatus(Policy.PolicyStatus.ACTIVE);

        claim = new Claim();
        claim.setId(1L);
        claim.setClient(client);
        claim.setPolicy(policy);
        claim.setType(Claim.ClaimType.LIFE);
        claim.setAmount(BigDecimal.valueOf(50000.0));
        claim.setDescription("Life insurance claim");
        claim.setStatus(Claim.ClaimStatus.SUBMITTED);

        request = new CreateClaimRequest();
        request.setClientId(1L);
        request.setPolicyId(1L);
        request.setType(Claim.ClaimType.LIFE);
        request.setAmount(BigDecimal.valueOf(50000.0));
        request.setDescription("Life insurance claim");

        currentUser = new User();
        currentUser.setId(99L);
        currentUser.setRole(User.Role.ADMIN);

        claimService = new ClaimService(claimRepository, policyRepository, new ClientAccessGuard(clientRepository));
    }

    @Test
    void getAllClaims_returnsListOfClaims() {
        when(claimRepository.findAll()).thenReturn(List.of(claim));

        List<ClaimResponse> result = claimService.getAllClaims(currentUser);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAmount()).isEqualByComparingTo(BigDecimal.valueOf(50000.0));
    }

    @Test
    void getClaimById_existingId_returnsClaim() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        ClaimResponse result = claimService.getClaimById(1L, currentUser);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(50000.0));
    }

    @Test
    void getClaimById_nonExistingId_throwsException() {
        when(claimRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claimService.getClaimById(99L, currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Claim not found with id: 99");
    }

    @Test
    void createClaim_validRequest_savesAndReturnsClaim() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(claimRepository.save(any(Claim.class))).thenReturn(claim);

        ClaimResponse result = claimService.createClaim(request, currentUser);

        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(50000.0));
        verify(claimRepository, times(1)).save(any(Claim.class));
    }

    @Test
    void createClaim_clientNotFound_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claimService.createClaim(request, currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with id: 1");

        verify(claimRepository, never()).save(any());
    }

    @Test
    void createClaim_policyNotFound_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claimService.createClaim(request, currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Policy not found with id: 1");

        verify(claimRepository, never()).save(any());
    }

    @Test
    void updateClaimStatus_existingId_updatesStatus() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        when(claimRepository.save(any(Claim.class))).thenReturn(claim);

        ClaimResponse result = claimService.updateClaimStatus(1L, Claim.ClaimStatus.APPROVED, currentUser);

        assertThat(result).isNotNull();
        verify(claimRepository, times(1)).save(any(Claim.class));
    }

    @Test
    void deleteClaim_existingId_deletesClaim() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        claimService.deleteClaim(1L, currentUser);

        verify(claimRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteClaim_nonExistingId_throwsException() {
        when(claimRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claimService.deleteClaim(99L, currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Claim not found with id: 99");

        verify(claimRepository, never()).deleteById(any());
    }
}
