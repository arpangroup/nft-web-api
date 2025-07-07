package com.trustai.income_service.rank.service;

import com.trustai.income_service.rank.entity.RankConfig;
import com.trustai.income_service.rank.evaluation.RankSpecification;
import com.trustai.income_service.rank.repository.RankConfigRepository;
import com.trustai.user_service.hierarchy.dto.UserMetrics;
import com.trustai.user_service.hierarchy.service.UserMetricsService;
import com.trustai.user_service.user.entity.User;
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

    public Optional<RankConfig> evaluate(User user) {
        UserMetrics metrics = metricsService.computeMetrics(user.getId());

        return rankRepo.findAllByActiveTrueOrderByRankOrderDesc().stream()
                .filter(rank -> isEligible(user, metrics, rank))
                .findFirst(); // Highest matched rank
    }

    private boolean isEligible(User user, UserMetrics metrics, RankConfig config) {
        return specifications.stream()
                .allMatch(spec -> spec.isSatisfied(user, metrics, config));
    }
}
