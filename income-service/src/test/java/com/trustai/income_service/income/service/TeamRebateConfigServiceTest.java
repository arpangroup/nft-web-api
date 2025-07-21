package com.trustai.income_service.income.service;

import com.trustai.income_service.income.entity.TeamRebateConfig;
import com.trustai.income_service.income.repository.TeamRebateConfigRepository;
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
public class TeamRebateConfigServiceTest {

    @InjectMocks
    private TeamRebateConfigService service;

    @Mock
    private TeamRebateConfigRepository repo;

    @Test
    void shouldReturnAllConfigs() {
        TeamRebateConfig config = new TeamRebateConfig("RANK_1", Map.of(1, BigDecimal.TEN));
        when(repo.findAll()).thenReturn(List.of(config));

        List<TeamRebateConfig> result = service.getAll();
        assertEquals(1, result.size());
        assertEquals("RANK_1", result.get(0).getRankCode());
    }

    @Test
    void shouldUpdateOrInsertConfigs() {
        TeamRebateConfig config = new TeamRebateConfig("RANK_1", Map.of(1, BigDecimal.TEN));

        when(repo.findById("RANK_1")).thenReturn(Optional.of(config));
        service.updateTeamConfigs(List.of(config));

        verify(repo).save(config);
    }
}
