package com.eeluwole.finance_api.rewards.dto;

import com.eeluwole.finance_api.rewards.Reward;
import java.time.LocalDateTime;

public class RewardResponse {

    private Long id;
    private Long clientId;
    private String clientName;
    private Integer points;
    private String reason;
    private Reward.RewardType type;
    private Reward.RewardStatus status;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public Reward.RewardStatus getStatus() {
        return status;
    }

    public void setStatus(Reward.RewardStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}