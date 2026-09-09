package com.eeluwole.finance_api.advanced;

import com.eeluwole.finance_api.advanced.dto.AdvancedResponse;
import com.eeluwole.finance_api.advanced.dto.CreateAdvancedRequest;
import com.eeluwole.finance_api.auth.User;
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
class AdvancedServiceTest {

    @Mock
    private AdvancedRepository advancedRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PolicyRepository policyRepository;

    private AdvancedService advancedService;

    private Client client;
    private Policy policy;
    private Advanced loan;
    private CreateAdvancedRequest request;
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

        loan = new Advanced();
        loan.setId(1L);
        loan.setClient(client);
        loan.setPolicy(policy);
        loan.setLoanAmount(BigDecimal.valueOf(10000.0));
        loan.setInterestRate(BigDecimal.valueOf(5.0));
        loan.setDueDate(LocalDate.of(2025, 6, 1));
        loan.setStatus(Advanced.LoanStatus.ACTIVE);

        request = new CreateAdvancedRequest();
        request.setClientId(1L);
        request.setPolicyId(1L);
        request.setLoanAmount(BigDecimal.valueOf(10000.0));
        request.setInterestRate(BigDecimal.valueOf(5.0));
        request.setDueDate(LocalDate.of(2025, 6, 1));

        currentUser = new User();
        currentUser.setId(99L);
        currentUser.setRole(User.Role.ADMIN);

        advancedService = new AdvancedService(advancedRepository, policyRepository, new ClientAccessGuard(clientRepository));
    }

    @Test
    void getAllLoans_returnsListOfLoans() {
        when(advancedRepository.findAll()).thenReturn(List.of(loan));

        List<AdvancedResponse> result = advancedService.getAllLoans(currentUser);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getLoanAmount()).isEqualByComparingTo(BigDecimal.valueOf(10000.0));
    }

    @Test
    void getLoanById_existingId_returnsLoan() {
        when(advancedRepository.findById(1L)).thenReturn(Optional.of(loan));

        AdvancedResponse result = advancedService.getLoanById(1L, currentUser);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getLoanAmount()).isEqualByComparingTo(BigDecimal.valueOf(10000.0));
    }

    @Test
    void getLoanById_nonExistingId_throwsException() {
        when(advancedRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> advancedService.getLoanById(99L, currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Loan not found with id: 99");
    }

    @Test
    void createLoan_validRequest_savesAndReturnsLoan() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(advancedRepository.save(any(Advanced.class))).thenReturn(loan);

        AdvancedResponse result = advancedService.createLoan(request, currentUser);

        assertThat(result.getLoanAmount()).isEqualByComparingTo(BigDecimal.valueOf(10000.0));
        verify(advancedRepository, times(1)).save(any(Advanced.class));
    }

    @Test
    void createLoan_clientNotFound_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> advancedService.createLoan(request, currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with id: 1");

        verify(advancedRepository, never()).save(any());
    }

    @Test
    void createLoan_policyNotFound_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> advancedService.createLoan(request, currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Policy not found with id: 1");

        verify(advancedRepository, never()).save(any());
    }

    @Test
    void updateLoanStatus_existingId_updatesStatus() {
        when(advancedRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(advancedRepository.save(any(Advanced.class))).thenReturn(loan);

        AdvancedResponse result = advancedService.updateLoanStatus(1L, Advanced.LoanStatus.REPAID, currentUser);

        assertThat(result).isNotNull();
        verify(advancedRepository, times(1)).save(any(Advanced.class));
    }

    @Test
    void deleteLoan_existingId_deletesLoan() {
        when(advancedRepository.findById(1L)).thenReturn(Optional.of(loan));

        advancedService.deleteLoan(1L, currentUser);

        verify(advancedRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteLoan_nonExistingId_throwsException() {
        when(advancedRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> advancedService.deleteLoan(99L, currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Loan not found with id: 99");

        verify(advancedRepository, never()).deleteById(any());
    }
}
