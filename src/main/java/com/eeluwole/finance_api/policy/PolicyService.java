package com.eeluwole.finance_api.policy;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientAccessGuard;
import com.eeluwole.finance_api.policy.dto.CreatePolicyRequest;
import com.eeluwole.finance_api.policy.dto.PolicyResponse;
import com.eeluwole.finance_api.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final ClientAccessGuard clientAccessGuard;

    public PolicyService(PolicyRepository policyRepository,
            ClientAccessGuard clientAccessGuard) {
        this.policyRepository = policyRepository;
        this.clientAccessGuard = clientAccessGuard;
    }

    // Get all policies (own only, unless ADMIN)
    public List<PolicyResponse> getAllPolicies(User currentUser) {
        return policyRepository.findAll()
                .stream()
                .filter(p -> clientAccessGuard.owns(p.getClient(), currentUser))
                .map(this::toResponse)
                .toList();
    }

    // Get policy by ID
    public PolicyResponse getPolicyById(Long id, User currentUser) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        clientAccessGuard.assertOwnership(policy.getClient(), currentUser);
        return toResponse(policy);
    }

    // Get policies by client
    public List<PolicyResponse> getPoliciesByClient(Long clientId, User currentUser) {
        clientAccessGuard.requireOwnedClient(clientId, currentUser);
        return policyRepository.findByClientId(clientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get policies by status (own only, unless ADMIN)
    public List<PolicyResponse> getPoliciesByStatus(Policy.PolicyStatus status, User currentUser) {
        return policyRepository.findByStatus(status)
                .stream()
                .filter(p -> clientAccessGuard.owns(p.getClient(), currentUser))
                .map(this::toResponse)
                .toList();
    }

    // Create policy
    public PolicyResponse createPolicy(CreatePolicyRequest request, User currentUser) {
        Client client = clientAccessGuard.requireOwnedClient(request.getClientId(), currentUser);

        if (policyRepository.existsByPolicyNumber(request.getPolicyNumber())) {
            throw new RuntimeException("Policy number already exists: " + request.getPolicyNumber());
        }

        Policy policy = new Policy();
        policy.setClient(client);
        policy.setPolicyNumber(request.getPolicyNumber());
        policy.setType(request.getType());
        policy.setCoverageAmount(request.getCoverageAmount());
        policy.setPremiumAmount(request.getPremiumAmount());
        policy.setStartDate(request.getStartDate());
        policy.setEndDate(request.getEndDate());

        return toResponse(policyRepository.save(policy));
    }

    // Update policy
    public PolicyResponse updatePolicy(Long id, CreatePolicyRequest request, User currentUser) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        clientAccessGuard.assertOwnership(policy.getClient(), currentUser);

        policy.setPolicyNumber(request.getPolicyNumber());
        policy.setType(request.getType());
        policy.setCoverageAmount(request.getCoverageAmount());
        policy.setPremiumAmount(request.getPremiumAmount());
        policy.setStartDate(request.getStartDate());
        policy.setEndDate(request.getEndDate());

        return toResponse(policyRepository.save(policy));
    }

    // Update policy status
    public PolicyResponse updatePolicyStatus(Long id, Policy.PolicyStatus status, User currentUser) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        clientAccessGuard.assertOwnership(policy.getClient(), currentUser);
        policy.setStatus(status);
        return toResponse(policyRepository.save(policy));
    }

    // Delete policy
    public void deletePolicy(Long id, User currentUser) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        clientAccessGuard.assertOwnership(policy.getClient(), currentUser);
        policyRepository.deleteById(id);
    }

    // Convert Policy → PolicyResponse DTO
    private PolicyResponse toResponse(Policy policy) {
        PolicyResponse response = new PolicyResponse();
        response.setId(policy.getId());
        response.setPolicyNumber(policy.getPolicyNumber());
        response.setType(policy.getType());
        response.setCoverageAmount(policy.getCoverageAmount());
        response.setPremiumAmount(policy.getPremiumAmount());
        response.setStartDate(policy.getStartDate());
        response.setEndDate(policy.getEndDate());
        response.setStatus(policy.getStatus());
        response.setCreatedAt(policy.getCreatedAt());
        response.setClientId(policy.getClient().getId());
        response.setClientName(policy.getClient().getFirstName()
                + " " + policy.getClient().getLastName());
        return response;
    }
}
