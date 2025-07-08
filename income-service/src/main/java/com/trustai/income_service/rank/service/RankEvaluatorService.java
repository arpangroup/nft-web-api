package com.trustai.income_service.rank.service;

import com.trustai.income_service.rank.dto.RankEvaluationResultDTO;
import com.trustai.income_service.rank.dto.SpecificationResult;
import com.trustai.income_service.rank.entity.RankConfig;
import com.trustai.income_service.rank.evaluation.RankSpecification;
import com.trustai.income_service.rank.repository.RankConfigRepository;
import com.trustai.user_service.hierarchy.dto.UserMetrics;
import com.trustai.user_service.hierarchy.service.UserMetricsService;
import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankEvaluatorService {
    private final RankConfigRepository rankRepo;
    private final List<RankSpecification> specifications;
    private final UserMetricsService metricsService;
    private final UserProfileService userService;

    /*public Optional<RankConfig> evaluate(User user) {
        UserMetrics metrics = metricsService.computeMetrics(user.getId());

        return rankRepo.findAllByActiveTrueOrderByRankOrderDesc().stream()
                .filter(rank -> isEligible(user, metrics, rank))
                .findFirst(); // Highest matched rank
    }*/

    public Optional<RankConfig> evaluate(User user) {
        UserMetrics metrics = metricsService.computeMetrics(user.getId());
        List<RankConfig> ranks = rankRepo.findAllByActiveTrueOrderByRankOrderDesc();

        RankConfig bestMatched = null;
        for (RankConfig rank : ranks) {
            log.info("🔍 Evaluating rank: {} ({})", rank.getDisplayName(), rank.getCode());

            List<SpecificationResult> results = specifications.stream()
                    .map(spec -> spec.evaluate(user, metrics, rank))
                    .toList();

            results.forEach(result -> log.info(" - [{}] Spec check: {}", rank.getCode(), result));

            boolean allPassed = results.stream().allMatch(SpecificationResult::isSatisfied);

            if (allPassed) {
                bestMatched = rank; // update best matched rank
            } else {
                // If no bestMatched yet, continue to check lower ranks
                // If bestMatched already found, break early because no need to check lower ranks
                if (bestMatched != null) {
                    log.info("❌ Rank NOT matched: {} ({}), stopping further evaluation.", rank.getDisplayName(), rank.getCode());
                    break;
                }
                // else no bestMatched yet, so continue checking next (lower) rank
                log.info("❌ Rank NOT matched: {} ({}), checking next lower rank.", rank.getDisplayName(), rank.getCode());
            }
        }

        if (bestMatched != null) {
            log.info("✅ Rank matched: {} ({})", bestMatched.getDisplayName(), bestMatched.getCode());
        } else {
            log.info("❌ No rank matched for user {}", user.getId());
        }
        return Optional.ofNullable(bestMatched);
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

    /*private boolean isEligible(User user, UserMetrics metrics, RankConfig config) {
        log.info("🔍 Evaluating rank: {} ({})", config.getDisplayName(), config.getCode());
//        return specifications.stream()
//                .allMatch(spec -> spec.isSatisfied(user, metrics, config));

        List<SpecificationResult> results = specifications.stream()
                .map(spec -> spec.evaluate(user, metrics, config))
                .toList();

        //results.forEach(result -> log.info("Rank check: {}", result));
        results.forEach(result -> {
            log.info(" - [{}] Spec check: {}", config.getCode(), result);
        });

        return results.stream().allMatch(SpecificationResult::isSatisfied);
    }*/
}
