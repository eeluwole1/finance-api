package com.eeluwole.finance_api.policy;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.policy.dto.CreatePolicyRequest;
import com.eeluwole.finance_api.policy.dto.PolicyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    // GET /api/v1/policies
    @GetMapping
    public ResponseEntity<List<PolicyResponse>> getAllPolicies(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(policyService.getAllPolicies(currentUser));
    }

    // GET /api/v1/policies/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> getPolicyById(@PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(policyService.getPolicyById(id, currentUser));
    }

    // GET /api/v1/policies/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PolicyResponse>> getPoliciesByClient(
            @PathVariable Long clientId, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(policyService.getPoliciesByClient(clientId, currentUser));
    }

    // GET /api/v1/policies/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PolicyResponse>> getPoliciesByStatus(
            @PathVariable Policy.PolicyStatus status, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(policyService.getPoliciesByStatus(status, currentUser));
    }

    // POST /api/v1/policies
    @PostMapping
    public ResponseEntity<PolicyResponse> createPolicy(
            @RequestBody CreatePolicyRequest request, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(policyService.createPolicy(request, currentUser));
    }

    // PUT /api/v1/policies/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PolicyResponse> updatePolicy(
            @PathVariable Long id,
            @RequestBody CreatePolicyRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(policyService.updatePolicy(id, request, currentUser));
    }

    // PATCH /api/v1/policies/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<PolicyResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Policy.PolicyStatus status,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(policyService.updatePolicyStatus(id, status, currentUser));
    }

    // DELETE /api/v1/policies/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePolicy(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        policyService.deletePolicy(id, currentUser);
        return ResponseEntity.ok("Policy " + id + " deleted successfully");
    }
}
