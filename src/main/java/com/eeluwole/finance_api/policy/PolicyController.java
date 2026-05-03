package com.eeluwole.finance_api.policy;

import com.eeluwole.finance_api.policy.dto.CreatePolicyRequest;
import com.eeluwole.finance_api.policy.dto.PolicyResponse;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<PolicyResponse>> getAllPolicies() {
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    // GET /api/v1/policies/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> getPolicyById(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.getPolicyById(id));
    }

    // GET /api/v1/policies/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PolicyResponse>> getPoliciesByClient(
            @PathVariable Long clientId) {
        return ResponseEntity.ok(policyService.getPoliciesByClient(clientId));
    }

    // GET /api/v1/policies/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PolicyResponse>> getPoliciesByStatus(
            @PathVariable Policy.PolicyStatus status) {
        return ResponseEntity.ok(policyService.getPoliciesByStatus(status));
    }

    // POST /api/v1/policies
    @PostMapping
    public ResponseEntity<PolicyResponse> createPolicy(
            @RequestBody CreatePolicyRequest request) {
        return ResponseEntity.ok(policyService.createPolicy(request));
    }

    // PUT /api/v1/policies/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PolicyResponse> updatePolicy(
            @PathVariable Long id,
            @RequestBody CreatePolicyRequest request) {
        return ResponseEntity.ok(policyService.updatePolicy(id, request));
    }

    // PATCH /api/v1/policies/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<PolicyResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Policy.PolicyStatus status) {
        return ResponseEntity.ok(policyService.updatePolicyStatus(id, status));
    }

    // DELETE /api/v1/policies/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePolicy(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return ResponseEntity.ok("Policy " + id + " deleted successfully");
    }
}