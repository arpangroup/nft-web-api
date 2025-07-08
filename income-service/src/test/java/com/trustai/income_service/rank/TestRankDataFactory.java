package com.trustai.income_service.rank;

import com.trustai.income_service.rank.entity.RankConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class TestRankDataFactory {

    public static List<RankConfig> getAllRanks() {
        // RANK_0:  MinDeposit: 15; No level requirement; No Txn possible;
        RankConfig rank0 = createRank("RANK_0", 0,
                new BigDecimal("15"), // minDepositAmount
                new BigDecimal("15"), // minInvestmentAmount
                new BigDecimal("0"), // commissionPercentage
                10, // rankBonus
                Map.of(1, 0, 2, 0, 3, 0) // requiredLevelCounts
        );

        // RANK_1: MinDeposit: 40; No level requirement; Only 1 txnPerDay; 1% Commission;
        RankConfig rank1 = createRank("RANK_1", 1,
                new BigDecimal("40"),  // minDepositAmount
                new BigDecimal("0"),   // minInvestmentAmount
                new BigDecimal("1.0"), // commissionPercentage
                20, // rankBonus
                Map.of(1, 0, 2, 0, 3, 0) // requiredLevelCounts
        );

        // RANK_2: MinDeposit: 300; Only 1 txnPerDay; 1.7% Commission; {LvA : 4, LvB : 5, LvC : 1}
        RankConfig rank2 = createRank("RANK_2", 2,
                new BigDecimal("300"), // minDepositAmount
                new BigDecimal("100"), // minInvestmentAmount
                new BigDecimal("1.7"), // commissionPercentage
                20, // rankBonus
                Map.of(1, 4, 2, 5, 3, 1) // requiredLevelCounts
        );

        // RANK_3: MinDeposit: 600; Only 1 txnPerDay; 1.7% Commission; {LvA : 6, LvB : 25, LvC : 5}
        RankConfig rank3 = createRank("RANK_3", 3,
                new BigDecimal("600"), // minDepositAmount
                new BigDecimal("200"), // minInvestmentAmount
                new BigDecimal("2.30"), // commissionPercentage
                20, // rankBonus
                Map.of(1, 6, 2, 25, 3, 5) // requiredLevelCounts
        );

        return List.of(rank0, rank1, rank2, rank3);
    }


    public static RankConfig createRank(String code, int order, BigDecimal minDeposit, BigDecimal minInvestmentAmount, BigDecimal commissionPercentage, int rankBonus, Map<Integer, Integer> levelCounts) {
        RankConfig config = new RankConfig();
        config.setCode(code);
        config.setRankOrder(order);
        config.setActive(true);
        config.setMinDepositAmount(minDeposit);
        config.setMinInvestmentAmount(minInvestmentAmount);
        config.setCommissionPercentage(commissionPercentage);
        config.setRankBonus(rankBonus);
        config.setRequiredLevelCounts(levelCounts);
        config.setMinDirectReferrals(levelCounts.get(1));
        config.setTxnPerDay(order == 0 ? 0 : 1);
        return config;
    }
}
