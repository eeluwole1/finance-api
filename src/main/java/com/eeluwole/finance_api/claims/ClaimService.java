package com.eeluwole.finance_api.claims;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientAccessGuard;
import com.eeluwole.finance_api.claims.dto.CreateClaimRequest;
import com.eeluwole.finance_api.claims.dto.ClaimResponse;
import com.eeluwole.finance_api.policy.Policy;
import com.eeluwole.finance_api.policy.PolicyRepository;
import com.eeluwole.finance_api.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    private final ClientAccessGuard clientAccessGuard;

    public ClaimService(ClaimRepository claimRepository,
            PolicyRepository policyRepository,
            ClientAccessGuard clientAccessGuard) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
        this.clientAccessGuard = clientAccessGuard;
    }

    public List<ClaimResponse> getAllClaims(User currentUser) {
        return claimRepository.findAll().stream()
                .filter(c -> clientAccessGuard.owns(c.getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public ClaimResponse getClaimById(Long id, User currentUser) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
        clientAccessGuard.assertOwnership(claim.getClient(), currentUser);
        return toResponse(claim);
    }

    public List<ClaimResponse> getClaimsByClient(Long clientId, User currentUser) {
        clientAccessGuard.requireOwnedClient(clientId, currentUser);
        return claimRepository.findByClientId(clientId).stream().map(this::toResponse).toList();
    }

    public List<ClaimResponse> getClaimsByPolicy(Long policyId, User currentUser) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + policyId));
        clientAccessGuard.assertOwnership(policy.getClient(), currentUser);
        return claimRepository.findByPolicyId(policyId).stream().map(this::toResponse).toList();
    }

    public List<ClaimResponse> getClaimsByStatus(Claim.ClaimStatus status, User currentUser) {
        return claimRepository.findByStatus(status).stream()
                .filter(c -> clientAccessGuard.owns(c.getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public ClaimResponse createClaim(CreateClaimRequest request, User currentUser) {
        Client client = clientAccessGuard.requireOwnedClient(request.getClientId(), currentUser);

        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + request.getPolicyId()));

        Claim claim = new Claim();
        claim.setClient(client);
        claim.setPolicy(policy);
        claim.setType(request.getType());
        claim.setAmount(request.getAmount());
        claim.setDescription(request.getDescription());

        return toResponse(claimRepository.save(claim));
    }

    public ClaimResponse updateClaimStatus(Long id, Claim.ClaimStatus status, User currentUser) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
        clientAccessGuard.assertOwnership(claim.getClient(), currentUser);
        claim.setStatus(status);
        return toResponse(claimRepository.save(claim));
    }

    public void deleteClaim(Long id, User currentUser) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
        clientAccessGuard.assertOwnership(claim.getClient(), currentUser);
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
