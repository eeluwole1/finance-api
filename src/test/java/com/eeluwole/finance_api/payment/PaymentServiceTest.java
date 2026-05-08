package com.eeluwole.finance_api.payment;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.payment.dto.CreatePaymentRequest;
import com.eeluwole.finance_api.payment.dto.PaymentResponse;
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
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Client client;
    private Policy policy;
    private Payment payment;
    private CreatePaymentRequest request;

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

        payment = new Payment();
        payment.setId(1L);
        payment.setClient(client);
        payment.setPolicy(policy);
        payment.setAmount(200.0);
        payment.setMethod(Payment.PaymentMethod.BANK_TRANSFER);
        payment.setStatus(Payment.PaymentStatus.PENDING);

        request = new CreatePaymentRequest();
        request.setClientId(1L);
        request.setPolicyId(1L);
        request.setAmount(200.0);
        request.setMethod(Payment.PaymentMethod.BANK_TRANSFER);
    }

    @Test
    void getAllPayments_returnsListOfPayments() {
        when(paymentRepository.findAll()).thenReturn(List.of(payment));

        List<PaymentResponse> result = paymentService.getAllPayments();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAmount()).isEqualTo(200.0);
    }

    @Test
    void getPaymentById_existingId_returnsPayment() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentResponse result = paymentService.getPaymentById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAmount()).isEqualTo(200.0);
    }

    @Test
    void getPaymentById_nonExistingId_throwsException() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.getPaymentById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Payment not found with id: 99");
    }

    @Test
    void createPayment_validRequest_savesAndReturnsPayment() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponse result = paymentService.createPayment(request);

        assertThat(result.getAmount()).isEqualTo(200.0);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void createPayment_clientNotFound_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.createPayment(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with id: 1");

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void createPayment_policyNotFound_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.createPayment(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Policy not found with id: 1");

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void updatePaymentStatus_existingId_updatesStatus() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponse result = paymentService.updatePaymentStatus(1L, Payment.PaymentStatus.COMPLETED);

        assertThat(result).isNotNull();
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void deletePayment_existingId_deletesPayment() {
        when(paymentRepository.existsById(1L)).thenReturn(true);

        paymentService.deletePayment(1L);

        verify(paymentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePayment_nonExistingId_throwsException() {
        when(paymentRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> paymentService.deletePayment(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Payment not found with id: 99");

        verify(paymentRepository, never()).deleteById(any());
    }
}
