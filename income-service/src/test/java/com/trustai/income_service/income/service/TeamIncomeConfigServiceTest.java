package com.trustai.income_service.income.service;

import com.trustai.income_service.income.entity.TeamIncomeConfig;
import com.trustai.income_service.income.repository.TeamIncomeConfigRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamIncomeConfigServiceTest {

    @InjectMocks
    private TeamIncomeConfigService service;

    @Mock
    private TeamIncomeConfigRepository repo;

    @Test
    void shouldReturnAllConfigs() {
        TeamIncomeConfig config = new TeamIncomeConfig("RANK_1", Map.of(1, BigDecimal.TEN));
        when(repo.findAll()).thenReturn(List.of(config));

        List<TeamIncomeConfig> result = service.getAll();
        assertEquals(1, result.size());
        assertEquals("RANK_1", result.get(0).getRankCode());
    }

    @Test
    void shouldUpdateOrInsertConfigs() {
        TeamIncomeConfig config = new TeamIncomeConfig("RANK_1", Map.of(1, BigDecimal.TEN));

        when(repo.findById("RANK_1")).thenReturn(Optional.of(config));
        service.updateTeamConfigs(List.of(config));

        verify(repo).save(config);
    }
}
