package com.eeluwole.finance_api.advanced;

import com.eeluwole.finance_api.advanced.dto.CreateAdvancedRequest;
import com.eeluwole.finance_api.advanced.dto.AdvancedResponse;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<AdvancedResponse>> getAllLoans() {
        return ResponseEntity.ok(advancedService.getAllLoans());
    }

    // GET /api/v1/loans/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AdvancedResponse> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(advancedService.getLoanById(id));
    }

    // GET /api/v1/loans/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AdvancedResponse>> getLoansByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(advancedService.getLoansByClient(clientId));
    }

    // GET /api/v1/loans/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AdvancedResponse>> getLoansByStatus(@PathVariable Advanced.LoanStatus status) {
        return ResponseEntity.ok(advancedService.getLoansByStatus(status));
    }

    // POST /api/v1/loans
    @PostMapping
    public ResponseEntity<AdvancedResponse> createLoan(@RequestBody CreateAdvancedRequest request) {
        return ResponseEntity.ok(advancedService.createLoan(request));
    }

    // PATCH /api/v1/loans/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<AdvancedResponse> updateLoanStatus(
            @PathVariable Long id,
            @RequestParam Advanced.LoanStatus status) {
        return ResponseEntity.ok(advancedService.updateLoanStatus(id, status));
    }

    // DELETE /api/v1/loans/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        advancedService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }
}