package com.trustai.income_service.income.service;

import com.trustai.income_service.income.entity.TeamRebateConfig;
import com.trustai.income_service.income.repository.TeamRebateConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamCommissionService {
    private final TeamRebateConfigRepository teamRebateConfigRepository;

    public BigDecimal getTeamCommissionPercentage(String rank, int depth) {
        log.info("getTeamCommissionPercentage for Rank: {}, depth: {}", rank,depth);
        /*
        // Replace with DB or config-based logic
        if (rankCode == Rank.RANK_2 && depth == 1) return BigDecimal.valueOf(0.12); // 12% Lv.A
        if (rankCode == Rank.RANK_2 && depth == 2) return BigDecimal.valueOf(0.05); // 5% Lv.B
        if (rankCode == Rank.RANK_2 && depth == 3) return BigDecimal.valueOf(0.02); // 2% Lv.C
        return BigDecimal.ZERO;
        */

        TeamRebateConfig config = teamRebateConfigRepository.findById(rank)
                .orElseThrow(() -> new IllegalStateException("No team config for rankCode: " + rank));

        BigDecimal teamIncomePercentage = config.getIncomePercentages().getOrDefault(depth, BigDecimal.ZERO);
        BigDecimal teamIncomeRate = teamIncomePercentage.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        log.info("Team IncomePercentage: {}% ===> Team IncomeRate: {} for Rank: {}, Depth: {}", teamIncomePercentage, teamIncomeRate, rank, depth);
        return teamIncomeRate;
    }

}
