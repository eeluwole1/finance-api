package com.eeluwole.finance_api.advanced;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientAccessGuard;
import com.eeluwole.finance_api.advanced.dto.CreateAdvancedRequest;
import com.eeluwole.finance_api.advanced.dto.AdvancedResponse;
import com.eeluwole.finance_api.policy.Policy;
import com.eeluwole.finance_api.policy.PolicyRepository;
import com.eeluwole.finance_api.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class AdvancedService {

    private final AdvancedRepository advancedRepository;
    private final PolicyRepository policyRepository;
    private final ClientAccessGuard clientAccessGuard;

    public AdvancedService(AdvancedRepository advancedRepository,
            PolicyRepository policyRepository,
            ClientAccessGuard clientAccessGuard) {
        this.advancedRepository = advancedRepository;
        this.policyRepository = policyRepository;
        this.clientAccessGuard = clientAccessGuard;
    }

    public List<AdvancedResponse> getAllLoans(User currentUser) {
        return advancedRepository.findAll().stream()
                .filter(l -> clientAccessGuard.owns(l.getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public AdvancedResponse getLoanById(Long id, User currentUser) {
        Advanced loan = advancedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
        clientAccessGuard.assertOwnership(loan.getClient(), currentUser);
        return toResponse(loan);
    }

    public List<AdvancedResponse> getLoansByClient(Long clientId, User currentUser) {
        clientAccessGuard.requireOwnedClient(clientId, currentUser);
        return advancedRepository.findByClientId(clientId).stream().map(this::toResponse).toList();
    }

    public List<AdvancedResponse> getLoansByStatus(Advanced.LoanStatus status, User currentUser) {
        return advancedRepository.findByStatus(status).stream()
                .filter(l -> clientAccessGuard.owns(l.getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public AdvancedResponse createLoan(CreateAdvancedRequest request, User currentUser) {
        Client client = clientAccessGuard.requireOwnedClient(request.getClientId(), currentUser);

        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + request.getPolicyId()));

        Advanced loan = new Advanced();
        loan.setClient(client);
        loan.setPolicy(policy);
        loan.setLoanAmount(request.getLoanAmount());
        loan.setInterestRate(request.getInterestRate());
        loan.setDueDate(request.getDueDate());

        return toResponse(advancedRepository.save(loan));
    }

    public AdvancedResponse updateLoanStatus(Long id, Advanced.LoanStatus status, User currentUser) {
        Advanced loan = advancedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
        clientAccessGuard.assertOwnership(loan.getClient(), currentUser);
        loan.setStatus(status);
        return toResponse(advancedRepository.save(loan));
    }

    public void deleteLoan(Long id, User currentUser) {
        Advanced loan = advancedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
        clientAccessGuard.assertOwnership(loan.getClient(), currentUser);
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
