package com.eeluwole.finance_api.rewards;

import com.eeluwole.finance_api.rewards.dto.CreateRewardRequest;
import com.eeluwole.finance_api.rewards.dto.RewardResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    // GET /api/v1/rewards
    @GetMapping
    public ResponseEntity<List<RewardResponse>> getAllRewards() {
        return ResponseEntity.ok(rewardService.getAllRewards());
    }

    // GET /api/v1/rewards/{id}
    @GetMapping("/{id}")
    public ResponseEntity<RewardResponse> getRewardById(@PathVariable Long id) {
        return ResponseEntity.ok(rewardService.getRewardById(id));
    }

    // GET /api/v1/rewards/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RewardResponse>> getRewardsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(rewardService.getRewardsByClient(clientId));
    }

    // GET /api/v1/rewards/client/{clientId}/points
    @GetMapping("/client/{clientId}/points")
    public ResponseEntity<Integer> getTotalPointsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(rewardService.getTotalPointsByClient(clientId));
    }

    // GET /api/v1/rewards/type/{type}
    @GetMapping("/type/{type}")
    public ResponseEntity<List<RewardResponse>> getRewardsByType(@PathVariable Reward.RewardType type) {
        return ResponseEntity.ok(rewardService.getRewardsByType(type));
    }

    // POST /api/v1/rewards
    @PostMapping
    public ResponseEntity<RewardResponse> createReward(@RequestBody CreateRewardRequest request) {
        return ResponseEntity.ok(rewardService.createReward(request));
    }

    // PATCH /api/v1/rewards/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<RewardResponse> updateRewardStatus(
            @PathVariable Long id,
            @RequestParam Reward.RewardStatus status) {
        return ResponseEntity.ok(rewardService.updateRewardStatus(id, status));
    }

    // DELETE /api/v1/rewards/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable Long id) {
        rewardService.deleteReward(id);
        return ResponseEntity.noContent().build();
    }
}