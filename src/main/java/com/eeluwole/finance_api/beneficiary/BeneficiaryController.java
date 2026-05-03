package com.eeluwole.finance_api.beneficiary;

import com.eeluwole.finance_api.beneficiary.dto.CreateBeneficiaryRequest;
import com.eeluwole.finance_api.beneficiary.dto.BeneficiaryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    // GET /api/v1/beneficiaries
    @GetMapping
    public ResponseEntity<List<BeneficiaryResponse>> getAllBeneficiaries() {
        return ResponseEntity.ok(beneficiaryService.getAllBeneficiaries());
    }

    // GET /api/v1/beneficiaries/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BeneficiaryResponse> getBeneficiaryById(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiaryService.getBeneficiaryById(id));
    }

    // GET /api/v1/beneficiaries/policy/{policyId}
    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<BeneficiaryResponse>> getBeneficiariesByPolicy(@PathVariable Long policyId) {
        return ResponseEntity.ok(beneficiaryService.getBeneficiariesByPolicy(policyId));
    }

    // POST /api/v1/beneficiaries
    @PostMapping
    public ResponseEntity<BeneficiaryResponse> createBeneficiary(@RequestBody CreateBeneficiaryRequest request) {
        return ResponseEntity.ok(beneficiaryService.createBeneficiary(request));
    }

    // PUT /api/v1/beneficiaries/{id}
    @PutMapping("/{id}")
    public ResponseEntity<BeneficiaryResponse> updateBeneficiary(
            @PathVariable Long id,
            @RequestBody CreateBeneficiaryRequest request) {
        return ResponseEntity.ok(beneficiaryService.updateBeneficiary(id, request));
    }

    // PATCH /api/v1/beneficiaries/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<BeneficiaryResponse> updateBeneficiaryStatus(
            @PathVariable Long id,
            @RequestParam Beneficiary.BeneficiaryStatus status) {
        return ResponseEntity.ok(beneficiaryService.updateBeneficiaryStatus(id, status));
    }

    // DELETE /api/v1/beneficiaries/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Long id) {
        beneficiaryService.deleteBeneficiary(id);
        return ResponseEntity.noContent().build();
    }
}