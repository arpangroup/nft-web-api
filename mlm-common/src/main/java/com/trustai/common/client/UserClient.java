package com.trustai.common.client;

import com.trustai.common.dto.UserInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserClient {
    List<UserInfo> getUsers();
    List<UserInfo> getUsers(List<Long> userIds);
    UserInfo getUserById(Long userId);
    String getRankCode(Long userId);
    void updateRank(Long userId, String newRankCode);

    // User Wallet + Deposit Balance:
    Optional<BigDecimal> findWalletBalanceById(Long userId);
    void updateWalletBalance(long userId, BigDecimal updatedAmount);
    Optional<BigDecimal> findDepositBalanceById(Long userId);
    void updateDepositBalance(long userId, BigDecimal updatedTotalDepositAmount);
}
