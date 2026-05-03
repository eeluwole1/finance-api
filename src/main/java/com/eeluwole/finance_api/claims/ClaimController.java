package com.eeluwole.finance_api.claims;

import com.eeluwole.finance_api.claims.dto.CreateClaimRequest;
import com.eeluwole.finance_api.claims.dto.ClaimResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    // GET /api/v1/claims
    @GetMapping
    public ResponseEntity<List<ClaimResponse>> getAllClaims() {
        return ResponseEntity.ok(claimService.getAllClaims());
    }

    // GET /api/v1/claims/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ClaimResponse> getClaimById(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getClaimById(id));
    }

    // GET /api/v1/claims/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(claimService.getClaimsByClient(clientId));
    }

    // GET /api/v1/claims/policy/{policyId}
    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByPolicy(@PathVariable Long policyId) {
        return ResponseEntity.ok(claimService.getClaimsByPolicy(policyId));
    }

    // GET /api/v1/claims/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByStatus(@PathVariable Claim.ClaimStatus status) {
        return ResponseEntity.ok(claimService.getClaimsByStatus(status));
    }

    // POST /api/v1/claims
    @PostMapping
    public ResponseEntity<ClaimResponse> createClaim(@RequestBody CreateClaimRequest request) {
        return ResponseEntity.ok(claimService.createClaim(request));
    }

    // PATCH /api/v1/claims/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<ClaimResponse> updateClaimStatus(
            @PathVariable Long id,
            @RequestParam Claim.ClaimStatus status) {
        return ResponseEntity.ok(claimService.updateClaimStatus(id, status));
    }

    // DELETE /api/v1/claims/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return ResponseEntity.noContent().build();
    }
}