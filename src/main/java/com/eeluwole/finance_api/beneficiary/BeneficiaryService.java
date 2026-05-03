package com.eeluwole.finance_api.beneficiary;

import com.eeluwole.finance_api.policy.Policy;
import com.eeluwole.finance_api.policy.PolicyRepository;
import com.eeluwole.finance_api.beneficiary.dto.CreateBeneficiaryRequest;
import com.eeluwole.finance_api.beneficiary.dto.BeneficiaryResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final PolicyRepository policyRepository;

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository,
            PolicyRepository policyRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.policyRepository = policyRepository;
    }

    public List<BeneficiaryResponse> getAllBeneficiaries() {
        return beneficiaryRepository.findAll().stream().map(this::toResponse).toList();
    }

    public BeneficiaryResponse getBeneficiaryById(Long id) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found with id: " + id));
        return toResponse(beneficiary);
    }

    public List<BeneficiaryResponse> getBeneficiariesByPolicy(Long policyId) {
        return beneficiaryRepository.findByPolicyId(policyId).stream().map(this::toResponse).toList();
    }

    public BeneficiaryResponse createBeneficiary(CreateBeneficiaryRequest request) {
        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + request.getPolicyId()));

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

    public BeneficiaryResponse updateBeneficiary(Long id, CreateBeneficiaryRequest request) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found with id: " + id));

        beneficiary.setFirstName(request.getFirstName());
        beneficiary.setLastName(request.getLastName());
        beneficiary.setEmail(request.getEmail());
        beneficiary.setPhone(request.getPhone());
        beneficiary.setRelationship(request.getRelationship());
        beneficiary.setPercentage(request.getPercentage());

        return toResponse(beneficiaryRepository.save(beneficiary));
    }

    public BeneficiaryResponse updateBeneficiaryStatus(Long id, Beneficiary.BeneficiaryStatus status) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found with id: " + id));
        beneficiary.setStatus(status);
        return toResponse(beneficiaryRepository.save(beneficiary));
    }

    public void deleteBeneficiary(Long id) {
        if (!beneficiaryRepository.existsById(id)) {
            throw new RuntimeException("Beneficiary not found with id: " + id);
        }
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