package com.trustai.income_service.rank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
public class RankConfigDto {
    private String rank;
    private int minWalletBalance;
    private int maxWalletBalance;
    private int txnPerDay;
    private int stakeValue;
    private BigDecimal commissionRate = BigDecimal.ZERO;
    private Map<Integer, Integer> requiredLevelCounts;
}
