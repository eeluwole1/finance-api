package com.eeluwole.finance_api.rewards;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findByClientId(Long clientId);

    List<Reward> findByType(Reward.RewardType type);

    List<Reward> findByStatus(Reward.RewardStatus status);
}