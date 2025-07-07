package com.trustai.income_service.rank.service;

import com.trustai.income_service.rank.dto.RankConfigDto;
import com.trustai.income_service.rank.entity.RankConfig;
import com.trustai.income_service.rank.repository.RankConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankConfigService {
    private final RankConfigRepository rankConfigRepository;

    public List<RankConfig> getAllRankConfigs() {
        return new ArrayList<>(rankConfigRepository.findAll());
    }

    public RankConfig getRankById(Long id) {
        return rankConfigRepository.findById(id).orElseThrow(() -> new RuntimeException("Id Not found"));
    }

    public RankConfig createRank(RankConfigDto request) {
        RankConfig rank = new RankConfig();
        rank.setCode(request.getCode());
        rank.setDisplayName(request.getDisplayName());
        rank.setRankOrder(request.getRankOrder());

        rank.setMinDepositAmount(request.getMinDepositAmount());
        rank.setMinInvestmentAmount(request.getMinInvestmentAmount());
        rank.setMinDirectReferrals(request.getMinDirectReferrals());
        rank.setMinReferralTotalDeposit(request.getMinReferralTotalDeposit());
        rank.setMinReferralTotalInvestment(request.getMinReferralTotalInvestment());
        rank.setMinTotalEarnings(request.getMinTotalEarnings());

        rank.setCommissionPercentage(request.getCommissionPercentage());
        rank.setRankBonus(request.getRankBonus());
        rank.setDescription(request.getDescription());
        rank.setActive(request.isActive());

        //rank.setRewardType();
        //rank.setRankType();

        rank.getRequiredLevelCounts().put(1, request.getMinLevel1Count());//getMinDirectReferrals
        rank.getRequiredLevelCounts().put(2, request.getMinLevel2Count());
        rank.getRequiredLevelCounts().put(3, request.getMinLevel3Count());

        return rankConfigRepository.save(rank);
    }

    public void patchRank(Long id, Map<String, Object> updates) {
        RankConfig rank = rankConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rank not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "code" -> rank.setCode((String) value);
                case "displayName" -> rank.setDisplayName((String) value);
                case "rankOrder" -> rank.setRankOrder(castToInt(value));
                case "minDepositAmount" -> rank.setMinDepositAmount(castToBigDecimal(value));
                case "minInvestmentAmount" -> rank.setMinInvestmentAmount(castToBigDecimal(value));
                case "minDirectReferrals" -> rank.setMinDirectReferrals(castToInt(value)); // stored in requiredLevelCounts[1]
                case "minReferralTotalDeposit" -> rank.setMinReferralTotalDeposit(castToBigDecimal(value));
                case "minReferralTotalInvestment" -> rank.setMinReferralTotalInvestment(castToBigDecimal(value));
                case "minTotalEarnings" -> rank.setMinTotalEarnings(castToBigDecimal(value));
                case "txnPerDay" -> rank.setTxnPerDay(castToInt(value));
                /*case "requiredLevelCounts" -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Integer> levels = (Map<String, Integer>) value;
                    levels.forEach((k, v) -> {
                        int depth = Integer.parseInt(k);
                        rank.getRequiredLevelCounts().put(depth, v);
                    });
                }*/
                case "commissionPercentage" -> rank.setCommissionPercentage(castToBigDecimal(value));
                case "rankBonus" -> rank.setRankBonus(castToInt(value));
                case "description" -> rank.setDescription((String) value);
                case "active" -> rank.setActive((Boolean) value);
                case "achievable" -> rank.setAchievable((Boolean) value);
                case "imageUrl" -> rank.setImageUrl((String) value);
                case "rewardType" -> rank.setRewardType(RankConfig.RewardType.valueOf(value.toString()));
                case "rankType" -> rank.setRankType(RankConfig.RankType.valueOf(value.toString()));

                // Handle level-specific fields
                case "minLevel1Count" -> rank.getRequiredLevelCounts().put(1, castToInt(value));
                case "minLevel2Count" -> rank.getRequiredLevelCounts().put(2, castToInt(value));
                case "minLevel3Count" -> rank.getRequiredLevelCounts().put(3, castToInt(value));
                default -> throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        rankConfigRepository.save(rank);
    }

    private int castToInt(Object value) {
        if (value instanceof Integer) return (int) value;
        return Integer.parseInt(value.toString());
    }

    private BigDecimal castToBigDecimal(Object value) {
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return BigDecimal.valueOf(((Number) value).doubleValue());
        return new BigDecimal(value.toString());
    }
}
