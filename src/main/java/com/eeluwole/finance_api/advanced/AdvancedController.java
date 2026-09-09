package com.eeluwole.finance_api.advanced;

import com.eeluwole.finance_api.advanced.dto.CreateAdvancedRequest;
import com.eeluwole.finance_api.advanced.dto.AdvancedResponse;
import com.eeluwole.finance_api.auth.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
public class AdvancedController {

    private final AdvancedService advancedService;

    public AdvancedController(AdvancedService advancedService) {
        this.advancedService = advancedService;
    }

    // GET /api/v1/loans
    @GetMapping
    public ResponseEntity<List<AdvancedResponse>> getAllLoans(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(advancedService.getAllLoans(currentUser));
    }

    // GET /api/v1/loans/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AdvancedResponse> getLoanById(@PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(advancedService.getLoanById(id, currentUser));
    }

    // GET /api/v1/loans/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AdvancedResponse>> getLoansByClient(@PathVariable Long clientId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(advancedService.getLoansByClient(clientId, currentUser));
    }

    // GET /api/v1/loans/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AdvancedResponse>> getLoansByStatus(@PathVariable Advanced.LoanStatus status,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(advancedService.getLoansByStatus(status, currentUser));
    }

    // POST /api/v1/loans
    @PostMapping
    public ResponseEntity<AdvancedResponse> createLoan(@Valid @RequestBody CreateAdvancedRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(advancedService.createLoan(request, currentUser));
    }

    // PATCH /api/v1/loans/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<AdvancedResponse> updateLoanStatus(
            @PathVariable Long id,
            @RequestParam Advanced.LoanStatus status,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(advancedService.updateLoanStatus(id, status, currentUser));
    }

    // DELETE /api/v1/loans/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        advancedService.deleteLoan(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
