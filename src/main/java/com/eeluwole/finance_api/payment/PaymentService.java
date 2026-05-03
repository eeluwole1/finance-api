package com.eeluwole.finance_api.payment;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.payment.dto.CreatePaymentRequest;
import com.eeluwole.finance_api.payment.dto.PaymentResponse;
import com.eeluwole.finance_api.policy.Policy;
import com.eeluwole.finance_api.policy.PolicyRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ClientRepository clientRepository;
    private final PolicyRepository policyRepository;

    public PaymentService(PaymentRepository paymentRepository,
            ClientRepository clientRepository,
            PolicyRepository policyRepository) {
        this.paymentRepository = paymentRepository;
        this.clientRepository = clientRepository;
        this.policyRepository = policyRepository;
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toResponse).toList();
    }

    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return toResponse(payment);
    }

    public List<PaymentResponse> getPaymentsByClient(Long clientId) {
        return paymentRepository.findByClientId(clientId).stream().map(this::toResponse).toList();
    }

    public List<PaymentResponse> getPaymentsByPolicy(Long policyId) {
        return paymentRepository.findByPolicyId(policyId).stream().map(this::toResponse).toList();
    }

    public List<PaymentResponse> getPaymentsByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    public PaymentResponse createPayment(CreatePaymentRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + request.getClientId()));

        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + request.getPolicyId()));

        Payment payment = new Payment();
        payment.setClient(client);
        payment.setPolicy(policy);
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());

        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse updatePaymentStatus(Long id, Payment.PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        payment.setStatus(status);
        return toResponse(paymentRepository.save(payment));
    }

    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setClientId(payment.getClient().getId());
        response.setClientName(payment.getClient().getFirstName() + " " + payment.getClient().getLastName());
        response.setPolicyId(payment.getPolicy().getId());
        response.setPolicyNumber(payment.getPolicy().getPolicyNumber());
        response.setAmount(payment.getAmount());
        response.setMethod(payment.getMethod());
        response.setStatus(payment.getStatus());
        response.setPaidAt(payment.getPaidAt());
        return response;
    }
}