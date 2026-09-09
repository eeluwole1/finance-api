package com.eeluwole.finance_api.payment;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.payment.dto.CreatePaymentRequest;
import com.eeluwole.finance_api.payment.dto.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<List<PaymentResponse>> getAllPayments(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(paymentService.getAllPayments(currentUser));
    }

    // GET /api/v1/payments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(paymentService.getPaymentById(id, currentUser));
    }

    // GET /api/v1/payments/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByClient(@PathVariable Long clientId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(paymentService.getPaymentsByClient(clientId, currentUser));
    }

    // GET /api/v1/payments/policy/{policyId}
    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByPolicy(@PathVariable Long policyId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(paymentService.getPaymentsByPolicy(policyId, currentUser));
    }

    // GET /api/v1/payments/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByStatus(@PathVariable Payment.PaymentStatus status,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(paymentService.getPaymentsByStatus(status, currentUser));
    }

    // POST /api/v1/payments
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody CreatePaymentRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(paymentService.createPayment(request, currentUser));
    }

    // PATCH /api/v1/payments/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam Payment.PaymentStatus status,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, status, currentUser));
    }

    // DELETE /api/v1/payments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        paymentService.deletePayment(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
