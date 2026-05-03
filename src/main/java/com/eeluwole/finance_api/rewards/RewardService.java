package com.eeluwole.finance_api.rewards;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.rewards.dto.CreateRewardRequest;
import com.eeluwole.finance_api.rewards.dto.RewardResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@SuppressWarnings("null")
public class RewardService {

    private final RewardRepository rewardRepository;
    private final ClientRepository clientRepository;

    public RewardService(RewardRepository rewardRepository,
            ClientRepository clientRepository) {
        this.rewardRepository = rewardRepository;
        this.clientRepository = clientRepository;
    }

    public List<RewardResponse> getAllRewards() {
        return rewardRepository.findAll().stream().map(this::toResponse).toList();
    }

    public RewardResponse getRewardById(Long id) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found with id: " + id));
        return toResponse(reward);
    }

    public List<RewardResponse> getRewardsByClient(Long clientId) {
        return rewardRepository.findByClientId(clientId).stream().map(this::toResponse).toList();
    }

    public Integer getTotalPointsByClient(Long clientId) {
        return rewardRepository.findByClientId(clientId)
                .stream()
                .filter(r -> r.getType() == Reward.RewardType.EARNED
                        && r.getStatus() == Reward.RewardStatus.ACTIVE)
                .mapToInt(Reward::getPoints)
                .sum();
    }

    public List<RewardResponse> getRewardsByType(Reward.RewardType type) {
        return rewardRepository.findByType(type).stream().map(this::toResponse).toList();
    }

    public RewardResponse createReward(CreateRewardRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + request.getClientId()));

        Reward reward = new Reward();
        reward.setClient(client);
        reward.setPoints(request.getPoints());
        reward.setReason(request.getReason());
        reward.setType(request.getType());

        return toResponse(rewardRepository.save(reward));
    }

    public RewardResponse updateRewardStatus(Long id, Reward.RewardStatus status) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found with id: " + id));
        reward.setStatus(status);
        return toResponse(rewardRepository.save(reward));
    }

    public void deleteReward(Long id) {
        if (!rewardRepository.existsById(id)) {
            throw new RuntimeException("Reward not found with id: " + id);
        }
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