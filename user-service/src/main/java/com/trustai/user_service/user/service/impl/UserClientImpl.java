package com.trustai.user_service.user.service.impl;

import com.trustai.common.client.UserClient;
import com.trustai.common.dto.UserInfo;
import com.trustai.user_service.user.mapper.UserMapper;
import com.trustai.user_service.user.service.UserProfileService;
import com.trustai.user_service.user.service.UserBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserClientImpl implements UserClient {
    private final UserProfileService profileService;
    private final UserBalanceService userBalanceService;
    private final UserMapper mapper;

    @Override
    public List<UserInfo> getUsers() {
        return profileService.getUsers().stream().map(mapper::mapTo).toList();
    }

    @Override
    public List<UserInfo> getUsers(List<Long> userIds) {
        return profileService.getUserByIds(userIds).stream().map(mapper::mapTo).toList();
    }

    @Override
    public UserInfo getUserById(Long userId) {
        var user = profileService.getUserById(userId);
        return mapper.mapTo(user);
    }

    @Override
    public String getRankCode(Long userId) {
        return profileService.getUserById(userId).getRankCode();
    }

    @Override
    public void updateRank(Long userId, String newRankCode) {
        profileService.updateUserRank(userId, newRankCode);
    }

    @Override
    public Optional<BigDecimal> findWalletBalanceById(Long userId) {
        return userBalanceService.findWalletBalanceById(userId);
    }

    @Override
    public void updateWalletBalance(long userId, BigDecimal updatedAmount) {
        userBalanceService.updateWalletBalance(userId, updatedAmount);
    }

    @Override
    public Optional<BigDecimal> findDepositBalanceById(Long userId) {
        return userBalanceService.findDepositBalanceById(userId);
    }

    @Override
    public void updateDepositBalance(long userId, BigDecimal updatedTotalDepositAmount) {
        userBalanceService.updateDepositBalance(userId, updatedTotalDepositAmount);
    }

}
