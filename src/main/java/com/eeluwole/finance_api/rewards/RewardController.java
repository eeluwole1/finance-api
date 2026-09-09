package com.eeluwole.finance_api.rewards;

import com.eeluwole.finance_api.auth.User;
import com.eeluwole.finance_api.rewards.dto.CreateRewardRequest;
import com.eeluwole.finance_api.rewards.dto.RewardResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<List<RewardResponse>> getAllRewards(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(rewardService.getAllRewards(currentUser));
    }

    // GET /api/v1/rewards/{id}
    @GetMapping("/{id}")
    public ResponseEntity<RewardResponse> getRewardById(@PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(rewardService.getRewardById(id, currentUser));
    }

    // GET /api/v1/rewards/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RewardResponse>> getRewardsByClient(@PathVariable Long clientId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(rewardService.getRewardsByClient(clientId, currentUser));
    }

    // GET /api/v1/rewards/client/{clientId}/points
    @GetMapping("/client/{clientId}/points")
    public ResponseEntity<Integer> getTotalPointsByClient(@PathVariable Long clientId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(rewardService.getTotalPointsByClient(clientId, currentUser));
    }

    // GET /api/v1/rewards/type/{type}
    @GetMapping("/type/{type}")
    public ResponseEntity<List<RewardResponse>> getRewardsByType(@PathVariable Reward.RewardType type,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(rewardService.getRewardsByType(type, currentUser));
    }

    // POST /api/v1/rewards
    @PostMapping
    public ResponseEntity<RewardResponse> createReward(@RequestBody CreateRewardRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(rewardService.createReward(request, currentUser));
    }

    // PATCH /api/v1/rewards/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<RewardResponse> updateRewardStatus(
            @PathVariable Long id,
            @RequestParam Reward.RewardStatus status,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(rewardService.updateRewardStatus(id, status, currentUser));
    }

    // DELETE /api/v1/rewards/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        rewardService.deleteReward(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
