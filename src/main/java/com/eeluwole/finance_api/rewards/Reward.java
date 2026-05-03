package com.eeluwole.finance_api.rewards;

import com.eeluwole.finance_api.client.Client;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private Integer points;
    private String reason;

    @Enumerated(EnumType.STRING)
    private RewardType type;

    @Enumerated(EnumType.STRING)
    private RewardStatus status = RewardStatus.ACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum RewardType {
        EARNED, REDEEMED
    }

    public enum RewardStatus {
        ACTIVE, EXPIRED, USED
    }
}