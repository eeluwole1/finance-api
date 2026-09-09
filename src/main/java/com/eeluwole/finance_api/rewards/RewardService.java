package com.eeluwole.finance_api.rewards;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientAccessGuard;
import com.eeluwole.finance_api.rewards.dto.CreateRewardRequest;
import com.eeluwole.finance_api.rewards.dto.RewardResponse;
import com.eeluwole.finance_api.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class RewardService {

    private final RewardRepository rewardRepository;
    private final ClientAccessGuard clientAccessGuard;

    public RewardService(RewardRepository rewardRepository,
            ClientAccessGuard clientAccessGuard) {
        this.rewardRepository = rewardRepository;
        this.clientAccessGuard = clientAccessGuard;
    }

    public List<RewardResponse> getAllRewards(User currentUser) {
        return rewardRepository.findAll().stream()
                .filter(r -> clientAccessGuard.owns(r.getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public RewardResponse getRewardById(Long id, User currentUser) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reward not found with id: " + id));
        clientAccessGuard.assertOwnership(reward.getClient(), currentUser);
        return toResponse(reward);
    }

    public List<RewardResponse> getRewardsByClient(Long clientId, User currentUser) {
        clientAccessGuard.requireOwnedClient(clientId, currentUser);
        return rewardRepository.findByClientId(clientId).stream().map(this::toResponse).toList();
    }

    public Integer getTotalPointsByClient(Long clientId, User currentUser) {
        clientAccessGuard.requireOwnedClient(clientId, currentUser);
        return rewardRepository.findByClientId(clientId)
                .stream()
                .filter(r -> r.getType() == Reward.RewardType.EARNED
                        && r.getStatus() == Reward.RewardStatus.ACTIVE)
                .mapToInt(Reward::getPoints)
                .sum();
    }

    public List<RewardResponse> getRewardsByType(Reward.RewardType type, User currentUser) {
        return rewardRepository.findByType(type).stream()
                .filter(r -> clientAccessGuard.owns(r.getClient(), currentUser))
                .map(this::toResponse).toList();
    }

    public RewardResponse createReward(CreateRewardRequest request, User currentUser) {
        Client client = clientAccessGuard.requireOwnedClient(request.getClientId(), currentUser);

        Reward reward = new Reward();
        reward.setClient(client);
        reward.setPoints(request.getPoints());
        reward.setReason(request.getReason());
        reward.setType(request.getType());

        return toResponse(rewardRepository.save(reward));
    }

    public RewardResponse updateRewardStatus(Long id, Reward.RewardStatus status, User currentUser) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reward not found with id: " + id));
        clientAccessGuard.assertOwnership(reward.getClient(), currentUser);
        reward.setStatus(status);
        return toResponse(rewardRepository.save(reward));
    }

    public void deleteReward(Long id, User currentUser) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reward not found with id: " + id));
        clientAccessGuard.assertOwnership(reward.getClient(), currentUser);
        rewardRepository.deleteById(id);
    }

    private RewardResponse toResponse(Reward reward) {
        RewardResponse response = new RewardResponse();
        response.setId(reward.getId());
        response.setClientId(reward.getClient().getId());
        response.setClientName(reward.getClient().getFirstName()
                + " " + reward.getClient().getLastName());
        response.setPoints(reward.getPoints());
        response.setReason(reward.getReason());
        response.setType(reward.getType());
        response.setStatus(reward.getStatus());
        response.setCreatedAt(reward.getCreatedAt());
        return response;
    }
}
