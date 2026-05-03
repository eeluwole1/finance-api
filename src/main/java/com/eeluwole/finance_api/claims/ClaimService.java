package com.eeluwole.finance_api.claims;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.claims.dto.CreateClaimRequest;
import com.eeluwole.finance_api.claims.dto.ClaimResponse;
import com.eeluwole.finance_api.policy.Policy;
import com.eeluwole.finance_api.policy.PolicyRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ClientRepository clientRepository;
    private final PolicyRepository policyRepository;

    public ClaimService(ClaimRepository claimRepository,
            ClientRepository clientRepository,
            PolicyRepository policyRepository) {
        this.claimRepository = claimRepository;
        this.clientRepository = clientRepository;
        this.policyRepository = policyRepository;
    }

    public List<ClaimResponse> getAllClaims() {
        return claimRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ClaimResponse getClaimById(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found with id: " + id));
        return toResponse(claim);
    }

    public List<ClaimResponse> getClaimsByClient(Long clientId) {
        return claimRepository.findByClientId(clientId).stream().map(this::toResponse).toList();
    }

    public List<ClaimResponse> getClaimsByPolicy(Long policyId) {
        return claimRepository.findByPolicyId(policyId).stream().map(this::toResponse).toList();
    }

    public List<ClaimResponse> getClaimsByStatus(Claim.ClaimStatus status) {
        return claimRepository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    public ClaimResponse createClaim(CreateClaimRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + request.getClientId()));

        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + request.getPolicyId()));

        Claim claim = new Claim();
        claim.setClient(client);
        claim.setPolicy(policy);
        claim.setType(request.getType());
        claim.setAmount(request.getAmount());
        claim.setDescription(request.getDescription());

        return toResponse(claimRepository.save(claim));
    }

    public ClaimResponse updateClaimStatus(Long id, Claim.ClaimStatus status) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found with id: " + id));
        claim.setStatus(status);
        return toResponse(claimRepository.save(claim));
    }

    public void deleteClaim(Long id) {
        if (!claimRepository.existsById(id)) {
            throw new RuntimeException("Claim not found with id: " + id);
        }
        claimRepository.deleteById(id);
    }

    private ClaimResponse toResponse(Claim claim) {
        ClaimResponse response = new ClaimResponse();
        response.setId(claim.getId());
        response.setClientId(claim.getClient().getId());
        response.setClientName(claim.getClient().getFirstName()
                + " " + claim.getClient().getLastName());
        response.setPolicyId(claim.getPolicy().getId());
        response.setPolicyNumber(claim.getPolicy().getPolicyNumber());
        response.setType(claim.getType());
        response.setAmount(claim.getAmount());
        response.setDescription(claim.getDescription());
        response.setStatus(claim.getStatus());
        response.setCreatedAt(claim.getCreatedAt());
        return response;
    }
}