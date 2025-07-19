package com.trustai.income_service.income.strategy;

import com.trustai.income_service.income.dto.UplineIncomeLog;
import com.trustai.user_service.user.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TeamIncomeStrategy {
    List<UplineIncomeLog> distributeTeamIncome(Long sourceUserId, String sourceUserRank, BigDecimal baseIncome, List<User> uplines, Map<Long, Integer> uplineDepthMap);
}
