package com.trustai.nft_app.dashboard.service;

import com.trustai.income_service.income.entity.IncomeHistory;
import com.trustai.income_service.income.repository.IncomeHistoryRepository;
import com.trustai.mlm_rank_service.repository.RankConfigRepository;
import com.trustai.income_service.income.entity.IncomeStatsProjection;
import com.trustai.nft_app.dashboard.dto.UserDashboardDTO;
import com.trustai.user_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardAggregationService {
    private final IncomeHistoryRepository incomeHistoryRepo;
    private final UserRepository userRepo;
    private final RankConfigRepository rankConfigRepo;

    public UserDashboardDTO getUserDashboardData(Long userId) {

        // Query 1: Wallet balance & rank
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String reservationRange = null;
        if (user.getRankCode() != null) {
            var rankOpt = rankConfigRepo.findByCode(user.getRankCode());
            if (rankOpt.isPresent()) {
                var rank = rankOpt.get();
                reservationRange = rank.getMinInvestmentAmount() + " - " + rank.getMaxInvestmentAmount();
            }
        }

        // Query 2: All income stats in one go
        List<IncomeStatsProjection> stats = incomeHistoryRepo.findIncomeStatsByUser(userId);

        // Build lookup map
        Map<IncomeHistory.IncomeType, IncomeStatsProjection> map =
                stats.stream().collect(Collectors.toMap(IncomeStatsProjection::getType, s -> s));

        // Helper method to safely get values
        Function<IncomeHistory.IncomeType, IncomeStatsProjection> get =
                type -> map.getOrDefault(type, new IncomeStatsProjection() {
                    public IncomeHistory.IncomeType getType() { return type; }
                    public BigDecimal getTotalIncome() { return BigDecimal.ZERO; }
                    public BigDecimal getTodayIncome() { return BigDecimal.ZERO; }
                    public BigDecimal getLast7DaysIncome() { return BigDecimal.ZERO; }
                });

        return UserDashboardDTO.builder()
                .walletBalance(user.getWalletBalance())
                .currentRank(user.getRankCode())
                .reservationRange(reservationRange)

                .totalDailyIncome(get.apply(IncomeHistory.IncomeType.DAILY).getTotalIncome())
                .todayDailyIncome(get.apply(IncomeHistory.IncomeType.DAILY).getTodayIncome())
                .last7DaysDailyIncome(get.apply(IncomeHistory.IncomeType.DAILY).getLast7DaysIncome())

                .totalTeamIncome(get.apply(IncomeHistory.IncomeType.TEAM).getTotalIncome())
                .todayTeamIncome(get.apply(IncomeHistory.IncomeType.TEAM).getTodayIncome())
                .last7DaysTeamIncome(get.apply(IncomeHistory.IncomeType.TEAM).getLast7DaysIncome())

                .totalReferralIncome(get.apply(IncomeHistory.IncomeType.REFERRAL).getTotalIncome())
                .todayReferralIncome(get.apply(IncomeHistory.IncomeType.REFERRAL).getTodayIncome())
                .last7DaysReferralIncome(get.apply(IncomeHistory.IncomeType.REFERRAL).getLast7DaysIncome())

                .totalReserveIncome(get.apply(IncomeHistory.IncomeType.RESERVE).getTotalIncome())
                .todayReserveIncome(get.apply(IncomeHistory.IncomeType.RESERVE).getTodayIncome())
                .last7DaysReserveIncome(get.apply(IncomeHistory.IncomeType.RESERVE).getLast7DaysIncome())

                .build();
    }
}
