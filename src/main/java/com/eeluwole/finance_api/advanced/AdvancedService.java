package com.eeluwole.finance_api.advanced;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.advanced.dto.CreateAdvancedRequest;
import com.eeluwole.finance_api.advanced.dto.AdvancedResponse;
import com.eeluwole.finance_api.policy.Policy;
import com.eeluwole.finance_api.policy.PolicyRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class AdvancedService {

    private final AdvancedRepository advancedRepository;
    private final ClientRepository clientRepository;
    private final PolicyRepository policyRepository;

    public AdvancedService(AdvancedRepository advancedRepository,
            ClientRepository clientRepository,
            PolicyRepository policyRepository) {
        this.advancedRepository = advancedRepository;
        this.clientRepository = clientRepository;
        this.policyRepository = policyRepository;
    }

    public List<AdvancedResponse> getAllLoans() {
        return advancedRepository.findAll().stream().map(this::toResponse).toList();
    }

    public AdvancedResponse getLoanById(Long id) {
        Advanced loan = advancedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
        return toResponse(loan);
    }

    public List<AdvancedResponse> getLoansByClient(Long clientId) {
        return advancedRepository.findByClientId(clientId).stream().map(this::toResponse).toList();
    }

    public List<AdvancedResponse> getLoansByStatus(Advanced.LoanStatus status) {
        return advancedRepository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    public AdvancedResponse createLoan(CreateAdvancedRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + request.getClientId()));

        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + request.getPolicyId()));

        Advanced loan = new Advanced();
        loan.setClient(client);
        loan.setPolicy(policy);
        loan.setLoanAmount(request.getLoanAmount());
        loan.setInterestRate(request.getInterestRate());
        loan.setDueDate(request.getDueDate());

        return toResponse(advancedRepository.save(loan));
    }

    public AdvancedResponse updateLoanStatus(Long id, Advanced.LoanStatus status) {
        Advanced loan = advancedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
        loan.setStatus(status);
        return toResponse(advancedRepository.save(loan));
    }

    public void deleteLoan(Long id) {
        if (!advancedRepository.existsById(id)) {
            throw new RuntimeException("Loan not found with id: " + id);
        }
        advancedRepository.deleteById(id);
    }

    private AdvancedResponse toResponse(Advanced loan) {
        AdvancedResponse response = new AdvancedResponse();
        response.setId(loan.getId());
        response.setClientId(loan.getClient().getId());
        response.setClientName(loan.getClient().getFirstName()
                + " " + loan.getClient().getLastName());
        response.setPolicyId(loan.getPolicy().getId());
        response.setPolicyNumber(loan.getPolicy().getPolicyNumber());
        response.setLoanAmount(loan.getLoanAmount());
        response.setInterestRate(loan.getInterestRate());
        response.setDueDate(loan.getDueDate());
        response.setStatus(loan.getStatus());
        response.setCreatedAt(loan.getCreatedAt());
        return response;
    }
}