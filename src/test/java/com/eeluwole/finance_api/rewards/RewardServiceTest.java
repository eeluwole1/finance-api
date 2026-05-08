package com.eeluwole.finance_api.rewards;

import com.eeluwole.finance_api.client.Client;
import com.eeluwole.finance_api.client.ClientRepository;
import com.eeluwole.finance_api.rewards.dto.CreateRewardRequest;
import com.eeluwole.finance_api.rewards.dto.RewardResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private RewardRepository rewardRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private RewardService rewardService;

    private Client client;
    private Reward reward;
    private CreateRewardRequest request;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Smith");
        client.setEmail("john@email.com");
        client.setPhone("647-555-1234");
        client.setAddress("123 Main St, Toronto");
        client.setStatus(Client.ClientStatus.ACTIVE);

        reward = new Reward();
        reward.setId(1L);
        reward.setClient(client);
        reward.setPoints(500);
        reward.setReason("Policy renewal bonus");
        reward.setType(Reward.RewardType.EARNED);
        reward.setStatus(Reward.RewardStatus.ACTIVE);

        request = new CreateRewardRequest();
        request.setClientId(1L);
        request.setPoints(500);
        request.setReason("Policy renewal bonus");
        request.setType(Reward.RewardType.EARNED);
    }

    @Test
    void getAllRewards_returnsListOfRewards() {
        when(rewardRepository.findAll()).thenReturn(List.of(reward));

        List<RewardResponse> result = rewardService.getAllRewards();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPoints()).isEqualTo(500);
    }

    @Test
    void getRewardById_existingId_returnsReward() {
        when(rewardRepository.findById(1L)).thenReturn(Optional.of(reward));

        RewardResponse result = rewardService.getRewardById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPoints()).isEqualTo(500);
    }

    @Test
    void getRewardById_nonExistingId_throwsException() {
        when(rewardRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rewardService.getRewardById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Reward not found with id: 99");
    }

    @Test
    void getTotalPointsByClient_activeEarnedRewards_returnsSum() {
        when(rewardRepository.findByClientId(1L)).thenReturn(List.of(reward));

        Integer total = rewardService.getTotalPointsByClient(1L);

        assertThat(total).isEqualTo(500);
    }

    @Test
    void getTotalPointsByClient_redeemedRewards_excludedFromSum() {
        reward.setType(Reward.RewardType.REDEEMED);
        when(rewardRepository.findByClientId(1L)).thenReturn(List.of(reward));

        Integer total = rewardService.getTotalPointsByClient(1L);

        assertThat(total).isEqualTo(0);
    }

    @Test
    void createReward_validRequest_savesAndReturnsReward() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(rewardRepository.save(any(Reward.class))).thenReturn(reward);

        RewardResponse result = rewardService.createReward(request);

        assertThat(result.getPoints()).isEqualTo(500);
        verify(rewardRepository, times(1)).save(any(Reward.class));
    }

    @Test
    void createReward_clientNotFound_throwsException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rewardService.createReward(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with id: 1");

        verify(rewardRepository, never()).save(any());
    }

    @Test
    void updateRewardStatus_existingId_updatesStatus() {
        when(rewardRepository.findById(1L)).thenReturn(Optional.of(reward));
        when(rewardRepository.save(any(Reward.class))).thenReturn(reward);

        RewardResponse result = rewardService.updateRewardStatus(1L, Reward.RewardStatus.USED);

        assertThat(result).isNotNull();
        verify(rewardRepository, times(1)).save(any(Reward.class));
    }

    @Test
    void deleteReward_existingId_deletesReward() {
        when(rewardRepository.existsById(1L)).thenReturn(true);

        rewardService.deleteReward(1L);

        verify(rewardRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteReward_nonExistingId_throwsException() {
        when(rewardRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> rewardService.deleteReward(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Reward not found with id: 99");

        verify(rewardRepository, never()).deleteById(any());
    }
}
