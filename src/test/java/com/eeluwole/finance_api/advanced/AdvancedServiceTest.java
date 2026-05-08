package com.eeluwole.finance_api.advanced;

import com.eeluwole.finance_api.advanced.dto.AdvancedResponse;
import com.eeluwole.finance_api.advanced.dto.CreateAdvancedRequest;
import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.policy.Policy;
import com.eeluwole.finance_api.policy.PolicyRepository;
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
class AdvancedServiceTest {

    @Mock
    private AdvancedRepository advancedRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private AdvancedService advancedService;

    private Client client;
    private Policy policy;
    private Advanced loan;
    private CreateAdvancedRequest request;

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

        loan = new Advanced();
        loan.setId(1L);
        loan.setClient(client);
        loan.setPolicy(policy);
        loan.setLoanAmount(10000.0);
        loan.setInterestRate(5.0);
        loan.setDueDate(LocalDate.of(2025, 6, 1));
        loan.setStatus(Advanced.LoanStatus.ACTIVE);

        request = new CreateAdvancedRequest();
        request.setClientId(1L);
        request.setPolicyId(1L);
        request.setLoanAmount(10000.0);
        request.setInterestRate(5.0);
        request.setDueDate(LocalDate.of(2025, 6, 1));
    }

    @Test
    void getAllLoans_returnsListOfLoans() {
        when(advancedRepository.findAll()).thenReturn(List.of(loan));

        List<AdvancedResponse> result = advancedService.getAllLoans();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getLoanAmount()).isEqualTo(10000.0);
    }

    @Test
    void getLoanById_existingId_returnsLoan() {
        when(advancedRepository.findById(1L)).thenReturn(Optional.of(loan));

        AdvancedResponse result = advancedService.getLoanById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getLoanAmount()).isEqualTo(10000.0);
    }

    @Test
    void getLoanById_nonExistingId_throwsException() {
        when(advancedRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> advancedService.getLoanById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Loan not found with id: 99");
    }

    @Test
    void createLoan_validRequest_savesAndReturnsLoan() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(advancedRepository.save(any(Advanced.class))).thenReturn(loan);

        AdvancedResponse result = advancedService.createLoan(request);

        assertThat(result.getLoanAmount()).isEqualTo(10000.0);
        verify(advancedRepository, times(1)).save(any(Advanced.class));
    }

    @Test
    void createLoan_clientNotFound_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> advancedService.createLoan(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with id: 1");

        verify(advancedRepository, never()).save(any());
    }

    @Test
    void createLoan_policyNotFound_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> advancedService.createLoan(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Policy not found with id: 1");

        verify(advancedRepository, never()).save(any());
    }

    @Test
    void updateLoanStatus_existingId_updatesStatus() {
        when(advancedRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(advancedRepository.save(any(Advanced.class))).thenReturn(loan);

        AdvancedResponse result = advancedService.updateLoanStatus(1L, Advanced.LoanStatus.REPAID);

        assertThat(result).isNotNull();
        verify(advancedRepository, times(1)).save(any(Advanced.class));
    }

    @Test
    void deleteLoan_existingId_deletesLoan() {
        when(advancedRepository.existsById(1L)).thenReturn(true);

        advancedService.deleteLoan(1L);

        verify(advancedRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteLoan_nonExistingId_throwsException() {
        when(advancedRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> advancedService.deleteLoan(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Loan not found with id: 99");

        verify(advancedRepository, never()).deleteById(any());
    }
}
