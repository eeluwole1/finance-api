package com.eeluwole.finance_api.policy;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.policy.dto.CreatePolicyRequest;
import com.eeluwole.finance_api.policy.dto.PolicyResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final ClientRepository clientRepository;

    public PolicyService(PolicyRepository policyRepository,
            ClientRepository clientRepository) {
        this.policyRepository = policyRepository;
        this.clientRepository = clientRepository;
    }

    // Get all policies
    public List<PolicyResponse> getAllPolicies() {
        return policyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get policy by ID
    public PolicyResponse getPolicyById(Long id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + id));
        return toResponse(policy);
    }

    // Get policies by client
    public List<PolicyResponse> getPoliciesByClient(Long clientId) {
        return policyRepository.findByClientId(clientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get policies by status
    public List<PolicyResponse> getPoliciesByStatus(Policy.PolicyStatus status) {
        return policyRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Create policy
    public PolicyResponse createPolicy(CreatePolicyRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + request.getClientId()));

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
    public PolicyResponse updatePolicy(Long id, CreatePolicyRequest request) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + id));

        policy.setPolicyNumber(request.getPolicyNumber());
        policy.setType(request.getType());
        policy.setCoverageAmount(request.getCoverageAmount());
        policy.setPremiumAmount(request.getPremiumAmount());
        policy.setStartDate(request.getStartDate());
        policy.setEndDate(request.getEndDate());

        return toResponse(policyRepository.save(policy));
    }

    // Update policy status
    public PolicyResponse updatePolicyStatus(Long id, Policy.PolicyStatus status) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + id));
        policy.setStatus(status);
        return toResponse(policyRepository.save(policy));
    }

    // Delete policy
    public void deletePolicy(Long id) {
        if (!policyRepository.existsById(id)) {
            throw new RuntimeException("Policy not found with id: " + id);
        }
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