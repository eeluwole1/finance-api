package com.eeluwole.finance_api.rewards.dto;

import com.eeluwole.finance_api.rewards.Reward;

public class CreateRewardRequest {

    private Long clientId;
    private Integer points;
    private String reason;
    private Reward.RewardType type;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Reward.RewardType getType() {
        return type;
    }

    public void setType(Reward.RewardType type) {
        this.type = type;
    }
}