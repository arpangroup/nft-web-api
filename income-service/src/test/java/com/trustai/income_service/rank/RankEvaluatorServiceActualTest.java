package com.trustai.income_service.rank;

import com.trustai.income_service.rank.entity.RankConfig;
import com.trustai.income_service.rank.evaluation.RankSpecification;
import com.trustai.income_service.rank.evaluation.strategy.MinDepositAmountSpec;
import com.trustai.income_service.rank.evaluation.strategy.MinDirectReferralsSpec;
import com.trustai.income_service.rank.evaluation.strategy.RequiredLevelCountSpec;
import com.trustai.income_service.rank.repository.RankConfigRepository;
import com.trustai.income_service.rank.service.RankEvaluatorService;
import com.trustai.user_service.hierarchy.dto.UserHierarchyStats;
import com.trustai.user_service.hierarchy.dto.UserMetrics;
import com.trustai.user_service.hierarchy.service.UserMetricsService;
import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.service.UserProfileService;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RankEvaluatorServiceActualTest {
    @Mock private RankConfigRepository rankRepo;
    @Mock private UserMetricsService metricsService;
    @InjectMocks private RankEvaluatorService evaluator;
    @Mock private UserProfileService userService;

    // Use real specs here
    private final List<RankSpecification> specifications = List.of(
            new MinDepositAmountSpec(),
            new MinDirectReferralsSpec(),
            new RequiredLevelCountSpec()
    );


    private User user;

    @BeforeEach
    void setup() {
        // Reflectively inject if needed (since it's final)
        ReflectionTestUtils.setField(evaluator, "specifications", specifications);

        user = new User();
        user.setId(1L);
        user.setRankCode("RANK_0");
    }

    @Test
    void shouldReturnEmpty_WhenUserDoesNotMeetAnyCriteria() {
        System.out.println("🔍 TEST STARTED");
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



    /*@Test
    void shouldAssignHighestMatchingRank() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setRankCode("RANK_!");

        UserMetrics metrics = new UserMetrics(*//* populate mock values *//*);

        RankConfig bronze = createRank("BRONZE", 1);
        RankConfig silver = createRank("SILVER", 2);
        RankConfig gold = createRank("GOLD", 3);

        List<RankConfig> ranks = List.of(gold, silver, bronze); // Descending

        Mockito.when(metricsService.computeMetrics(1L)).thenReturn(metrics);
        Mockito.when(rankRepo.findAllByActiveTrueOrderByRankOrderDesc()).thenReturn(ranks);

        // All specs are mocked to return true
        Mockito.when(specifications.stream()).thenReturn(Stream.of(
                (RankSpecification) (u, m, r) -> true
        ));

        // When
        Optional<RankConfig> result = evaluator.evaluate(user);

        // Then
        assertTrue(result.isPresent());
        assertEquals("GOLD", result.get().getCode());
    }

    private RankConfig createRank(String code, int order) {
        RankConfig config = new RankConfig();
        config.setCode(code);
        config.setRankOrder(order);
        config.setActive(true);
        return config;
    }*/

}
