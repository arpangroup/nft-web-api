package com.trustai.mlm_rank_service;

import com.trustai.common.dto.UserInfo;
import com.trustai.mlm_rank_service.entity.RankConfig;
import com.trustai.mlm_rank_service.evaluation.DirectReferralSpec;
import com.trustai.mlm_rank_service.evaluation.MinimumDepositAmountSpec;
import com.trustai.mlm_rank_service.evaluation.RankSpecification;
import com.trustai.mlm_rank_service.evaluation.RequiredLevelCountsSpec;
import com.trustai.mlm_rank_service.repository.RankConfigRepository;
import com.trustai.mlm_rank_service.service.RankEvaluatorService;
import com.trustai.user_service.hierarchy.dto.UserHierarchyStats;
import com.trustai.user_service.hierarchy.dto.UserMetrics;
import com.trustai.user_service.hierarchy.service.UserMetricsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RankEvaluatorServiceTest {
    @Mock private RankConfigRepository rankRepo;
    @Mock private UserMetricsService metricsService;
    @InjectMocks private RankEvaluatorService evaluator;
    //@Mock private List<RankSpecification> specifications;

    private final List<RankSpecification> specifications = List.of(
            new MinimumDepositAmountSpec(),
            new DirectReferralSpec(),
            new RequiredLevelCountsSpec()
    );

    private UserInfo user;

    @BeforeEach
    void setup() {
        // Reflectively inject if needed (since it's final)
        ReflectionTestUtils.setField(evaluator, "specifications", specifications);

        user = new UserInfo();
        user.setId(1L);
        user.setRankCode("RANK_0");
    }

    @Test
    void shouldMatchRank_WhenAllCriteriaSatisfied() {
        // Arrange: User metrics
        UserMetrics metrics = UserMetrics.builder()
                .directReferrals(0)
                .totalDeposit(new BigDecimal("15"))
                .userHierarchyStats(UserHierarchyStats.builder()
                        .depthWiseCounts(Map.of(1, 0L, 2, 0L, 3, 0L))
                        .build())
                .build();

        when(metricsService.computeMetrics(user.getId())).thenReturn(metrics);
        when(rankRepo.findAllByActiveTrueOrderByRankOrderDesc()).thenReturn(TestRankDataFactory.getAllRanks());

        // Act
        Optional<RankConfig> result = evaluator.evaluate(user);

        // Assert
        assertTrue(result.isPresent());
        Assertions.assertEquals("RANK_0", result.get().getCode());
    }

    @Test
    void shouldAssignHighestMatchingRank() {
        UserMetrics metrics = UserMetrics.builder()
                .directReferrals(10)
                .totalDeposit(new BigDecimal("700"))
                .userHierarchyStats(UserHierarchyStats.builder()
                        .depthWiseCounts(Map.of(1, 6L, 2, 25L, 3, 5L))
                        .build())
                .build();

        when(metricsService.computeMetrics(user.getId())).thenReturn(metrics);
        when(rankRepo.findAllByActiveTrueOrderByRankOrderDesc()).thenReturn(TestRankDataFactory.getAllRanks());

        Optional<RankConfig> matched = evaluator.evaluate(user);

        assertTrue(matched.isPresent());
        Assertions.assertEquals("RANK_3", matched.get().getCode());
    }

    @Test
    void shouldMatchLowerRankWhenHighRankFails() {
        UserMetrics metrics = UserMetrics.builder()
                .directReferrals(6)
                .totalDeposit(new BigDecimal("320"))
                .totalInvestment(new BigDecimal("120"))
                .userHierarchyStats(UserHierarchyStats.builder()
                        .depthWiseCounts(Map.of(1, 4L, 2, 5L, 3, 1L))
                        .build())
                .build();

        when(metricsService.computeMetrics(user.getId())).thenReturn(metrics);
        when(rankRepo.findAllByActiveTrueOrderByRankOrderDesc()).thenReturn(TestRankDataFactory.getAllRanks());

        Optional<RankConfig> matched = evaluator.evaluate(user);

        assertTrue(matched.isPresent());
        Assertions.assertEquals("RANK_2", matched.get().getCode());
    }

    @Test
    void shouldReturnEmpty_WhenUserDoesNotMeetAnyCriteria() {
        // System.out.println("🔍 TEST STARTED");
        UserMetrics metrics = UserMetrics.builder()
                .directReferrals(1)
                .totalDeposit(new BigDecimal("5"))
                .userHierarchyStats(UserHierarchyStats.builder()
                        .depthWiseCounts(Map.of(1, 0L, 2, 0L, 3, 0L))
                        .build())
                .build();

        when(metricsService.computeMetrics(user.getId())).thenReturn(metrics);
        when(rankRepo.findAllByActiveTrueOrderByRankOrderDesc()).thenReturn(TestRankDataFactory.getAllRanks());

        Optional<RankConfig> result = evaluator.evaluate(user);

        assertTrue(result.isEmpty());
    }

}
