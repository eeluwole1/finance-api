package com.eeluwole.finance_api.payment;

import com.eeluwole.finance_api.payment.dto.CreatePaymentRequest;
import com.eeluwole.finance_api.payment.dto.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // GET /api/v1/payments
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // GET /api/v1/payments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    // GET /api/v1/payments/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(paymentService.getPaymentsByClient(clientId));
    }

    // GET /api/v1/payments/policy/{policyId}
    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByPolicy(@PathVariable Long policyId) {
        return ResponseEntity.ok(paymentService.getPaymentsByPolicy(policyId));
    }

    // GET /api/v1/payments/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByStatus(@PathVariable Payment.PaymentStatus status) {
        return ResponseEntity.ok(paymentService.getPaymentsByStatus(status));
    }

    // POST /api/v1/payments
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPayment(request));
    }

    // PATCH /api/v1/payments/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam Payment.PaymentStatus status) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, status));
    }

    // DELETE /api/v1/payments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}