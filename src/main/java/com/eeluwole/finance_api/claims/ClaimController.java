package com.eeluwole.finance_api.claims;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.claims.dto.CreateClaimRequest;
import com.eeluwole.finance_api.claims.dto.ClaimResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<List<ClaimResponse>> getAllClaims(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(claimService.getAllClaims(currentUser));
    }

    // GET /api/v1/claims/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ClaimResponse> getClaimById(@PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(claimService.getClaimById(id, currentUser));
    }

    // GET /api/v1/claims/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByClient(@PathVariable Long clientId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(claimService.getClaimsByClient(clientId, currentUser));
    }

    // GET /api/v1/claims/policy/{policyId}
    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByPolicy(@PathVariable Long policyId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(claimService.getClaimsByPolicy(policyId, currentUser));
    }

    // GET /api/v1/claims/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByStatus(@PathVariable Claim.ClaimStatus status,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(claimService.getClaimsByStatus(status, currentUser));
    }

    // POST /api/v1/claims
    @PostMapping
    public ResponseEntity<ClaimResponse> createClaim(@Valid @RequestBody CreateClaimRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(claimService.createClaim(request, currentUser));
    }

    // PATCH /api/v1/claims/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<ClaimResponse> updateClaimStatus(
            @PathVariable Long id,
            @RequestParam Claim.ClaimStatus status,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(claimService.updateClaimStatus(id, status, currentUser));
    }

    // DELETE /api/v1/claims/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        claimService.deleteClaim(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
