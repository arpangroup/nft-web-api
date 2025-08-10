package com.trustai.nft_app.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserDashboardDTO {
    private BigDecimal walletBalance;
    private String currentRank;
    private String reservationRange;

    private BigDecimal totalDailyIncome;
    private BigDecimal todayDailyIncome;
    private BigDecimal last7DaysDailyIncome;

    private BigDecimal totalTeamIncome;
    private BigDecimal todayTeamIncome;
    private BigDecimal last7DaysTeamIncome;

    private BigDecimal totalReferralIncome;
    private BigDecimal todayReferralIncome;
    private BigDecimal last7DaysReferralIncome;

    private BigDecimal totalReserveIncome;
    private BigDecimal todayReserveIncome;
    private BigDecimal last7DaysReserveIncome;
}
