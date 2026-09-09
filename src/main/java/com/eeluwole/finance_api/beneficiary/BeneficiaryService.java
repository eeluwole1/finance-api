package com.eeluwole.finance_api.beneficiary;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.client.ClientAccessGuard;
import com.eeluwole.finance_api.policy.Policy;
import com.eeluwole.finance_api.policy.PolicyRepository;
import com.eeluwole.finance_api.beneficiary.dto.CreateBeneficiaryRequest;
import com.eeluwole.finance_api.beneficiary.dto.BeneficiaryResponse;
import com.eeluwole.finance_api.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final PolicyRepository policyRepository;
    private final ClientAccessGuard clientAccessGuard;

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository,
            PolicyRepository policyRepository,
            ClientAccessGuard clientAccessGuard) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.policyRepository = policyRepository;
        this.clientAccessGuard = clientAccessGuard;
    }

    public List<BeneficiaryResponse> getAllBeneficiaries(User currentUser) {
        return beneficiaryRepository.findAll().stream()
                .filter(b -> clientAccessGuard.owns(b.getPolicy().getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public BeneficiaryResponse getBeneficiaryById(Long id, User currentUser) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + id));
        clientAccessGuard.assertOwnership(beneficiary.getPolicy().getClient(), currentUser);
        return toResponse(beneficiary);
    }

    public List<BeneficiaryResponse> getBeneficiariesByPolicy(Long policyId, User currentUser) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + policyId));
        clientAccessGuard.assertOwnership(policy.getClient(), currentUser);
        return beneficiaryRepository.findByPolicyId(policyId).stream().map(this::toResponse).toList();
    }

    public BeneficiaryResponse createBeneficiary(CreateBeneficiaryRequest request, User currentUser) {
        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + request.getPolicyId()));
        clientAccessGuard.assertOwnership(policy.getClient(), currentUser);

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setPolicy(policy);
        beneficiary.setFirstName(request.getFirstName());
        beneficiary.setLastName(request.getLastName());
        beneficiary.setEmail(request.getEmail());
        beneficiary.setPhone(request.getPhone());
        beneficiary.setRelationship(request.getRelationship());
        beneficiary.setPercentage(request.getPercentage());

        return toResponse(beneficiaryRepository.save(beneficiary));
    }

    public BeneficiaryResponse updateBeneficiary(Long id, CreateBeneficiaryRequest request, User currentUser) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + id));
        clientAccessGuard.assertOwnership(beneficiary.getPolicy().getClient(), currentUser);

        beneficiary.setFirstName(request.getFirstName());
        beneficiary.setLastName(request.getLastName());
        beneficiary.setEmail(request.getEmail());
        beneficiary.setPhone(request.getPhone());
        beneficiary.setRelationship(request.getRelationship());
        beneficiary.setPercentage(request.getPercentage());

        return toResponse(beneficiaryRepository.save(beneficiary));
    }

    public BeneficiaryResponse updateBeneficiaryStatus(Long id, Beneficiary.BeneficiaryStatus status, User currentUser) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + id));
        clientAccessGuard.assertOwnership(beneficiary.getPolicy().getClient(), currentUser);
        beneficiary.setStatus(status);
        return toResponse(beneficiaryRepository.save(beneficiary));
    }

    public void deleteBeneficiary(Long id, User currentUser) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + id));
        clientAccessGuard.assertOwnership(beneficiary.getPolicy().getClient(), currentUser);
        beneficiaryRepository.deleteById(id);
    }

    private BeneficiaryResponse toResponse(Beneficiary beneficiary) {
        BeneficiaryResponse response = new BeneficiaryResponse();
        response.setId(beneficiary.getId());
        response.setPolicyId(beneficiary.getPolicy().getId());
        response.setPolicyNumber(beneficiary.getPolicy().getPolicyNumber());
        response.setFirstName(beneficiary.getFirstName());
        response.setLastName(beneficiary.getLastName());
        response.setEmail(beneficiary.getEmail());
        response.setPhone(beneficiary.getPhone());
        response.setRelationship(beneficiary.getRelationship());
        response.setPercentage(beneficiary.getPercentage());
        response.setStatus(beneficiary.getStatus());
        response.setCreatedAt(beneficiary.getCreatedAt());
        return response;
    }
}
