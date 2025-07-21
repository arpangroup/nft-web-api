package com.trustai.income_service.income.service;

import com.trustai.income_service.income.entity.TeamRebateConfig;
import com.trustai.income_service.income.repository.TeamRebateConfigRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamCommissionServiceTest {
    @InjectMocks
    private TeamCommissionService service;

    @Mock
    private TeamRebateConfigRepository repo;

    @Test
    void shouldReturnCorrectPercentage_whenRankAndDepthExist() {
        String rank = "RANK_3";
        int depth = 1;
        BigDecimal percentage = new BigDecimal("12");

        TeamRebateConfig config = new TeamRebateConfig(rank, Map.of(depth, percentage));
        when(repo.findById(rank)).thenReturn(Optional.of(config));

        BigDecimal result = service.getTeamCommissionPercentage(rank, depth);
        assertEquals(new BigDecimal("0.12"), result);
    }

    @Test
    void shouldReturnZero_whenDepthNotInMap() {
        String rank = "RANK_3";
        TeamRebateConfig config = new TeamRebateConfig(rank, Map.of());
        when(repo.findById(rank)).thenReturn(Optional.of(config));

        BigDecimal result = service.getTeamCommissionPercentage(rank, 5);
        assertEquals(BigDecimal.ZERO.setScale(2), result);
    }

    @Test
    void shouldThrowException_whenRankNotFound() {
        when(repo.findById("UNKNOWN")).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () ->
                service.getTeamCommissionPercentage("UNKNOWN", 1)
        );
    }
}
