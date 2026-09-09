package com.eeluwole.finance_api.payment;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientAccessGuard;
import com.eeluwole.finance_api.payment.dto.CreatePaymentRequest;
import com.eeluwole.finance_api.payment.dto.PaymentResponse;
import com.eeluwole.finance_api.policy.Policy;
import com.eeluwole.finance_api.policy.PolicyRepository;
import com.eeluwole.finance_api.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PolicyRepository policyRepository;
    private final ClientAccessGuard clientAccessGuard;

    public PaymentService(PaymentRepository paymentRepository,
            PolicyRepository policyRepository,
            ClientAccessGuard clientAccessGuard) {
        this.paymentRepository = paymentRepository;
        this.policyRepository = policyRepository;
        this.clientAccessGuard = clientAccessGuard;
    }

    public List<PaymentResponse> getAllPayments(User currentUser) {
        return paymentRepository.findAll().stream()
                .filter(p -> clientAccessGuard.owns(p.getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public PaymentResponse getPaymentById(Long id, User currentUser) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        clientAccessGuard.assertOwnership(payment.getClient(), currentUser);
        return toResponse(payment);
    }

    public List<PaymentResponse> getPaymentsByClient(Long clientId, User currentUser) {
        clientAccessGuard.requireOwnedClient(clientId, currentUser);
        return paymentRepository.findByClientId(clientId).stream().map(this::toResponse).toList();
    }

    public List<PaymentResponse> getPaymentsByPolicy(Long policyId, User currentUser) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + policyId));
        clientAccessGuard.assertOwnership(policy.getClient(), currentUser);
        return paymentRepository.findByPolicyId(policyId).stream().map(this::toResponse).toList();
    }

    public List<PaymentResponse> getPaymentsByStatus(Payment.PaymentStatus status, User currentUser) {
        return paymentRepository.findByStatus(status).stream()
                .filter(p -> clientAccessGuard.owns(p.getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public PaymentResponse createPayment(CreatePaymentRequest request, User currentUser) {
        Client client = clientAccessGuard.requireOwnedClient(request.getClientId(), currentUser);

        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + request.getPolicyId()));

        Payment payment = new Payment();
        payment.setClient(client);
        payment.setPolicy(policy);
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());

        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse updatePaymentStatus(Long id, Payment.PaymentStatus status, User currentUser) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        clientAccessGuard.assertOwnership(payment.getClient(), currentUser);
        payment.setStatus(status);
        return toResponse(paymentRepository.save(payment));
    }

    public void deletePayment(Long id, User currentUser) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        clientAccessGuard.assertOwnership(payment.getClient(), currentUser);
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
