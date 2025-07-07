package com.trustai.income_service.rank.service;

import com.trustai.income_service.rank.dto.RankEvaluationResultDTO;
import com.trustai.income_service.rank.entity.RankConfig;
import com.trustai.income_service.rank.evaluation.RankSpecification;
import com.trustai.income_service.rank.repository.RankConfigRepository;
import com.trustai.user_service.hierarchy.dto.UserMetrics;
import com.trustai.user_service.hierarchy.service.UserMetricsService;
import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RankEvaluatorService {
    private final RankConfigRepository rankRepo;
    private final List<RankSpecification> specifications;
    private final UserMetricsService metricsService;
    private final UserProfileService userService;

    public Optional<RankConfig> evaluate(User user) {
        UserMetrics metrics = metricsService.computeMetrics(user.getId());

        return rankRepo.findAllByActiveTrueOrderByRankOrderDesc().stream()
                .filter(rank -> isEligible(user, metrics, rank))
                .findFirst(); // Highest matched rank
    }

    public RankEvaluationResultDTO evaluateAndUpdateRank(Long userId) {
        User user = userService.getUserById(userId);

        String oldRankCode = user.getRankCode(); // assuming you store rankCode
        Optional<RankConfig> matchedRank = evaluate(user);

        if (matchedRank.isPresent() && !matchedRank.get().getCode().equals(oldRankCode)) {
            user.setRankCode(matchedRank.get().getCode());
            userService.updateUserRank(userId, matchedRank.get().getCode()); // persist the new rank
            return new RankEvaluationResultDTO(userId, oldRankCode, matchedRank.get().getCode(), true, "Rank upgraded");
        }

        return new RankEvaluationResultDTO(userId, oldRankCode, oldRankCode, false, "No upgrade criteria met");
    }

    public List<RankEvaluationResultDTO> evaluateAndUpdateRanks(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            userIds = userService.getUsers().stream().map(User::getId).toList();
        }
        return userIds.stream()
                .map(this::evaluateAndUpdateRank)
                .toList();
    }

    private boolean isEligible(User user, UserMetrics metrics, RankConfig config) {
        return specifications.stream()
                .allMatch(spec -> spec.isSatisfied(user, metrics, config));
    }
}
