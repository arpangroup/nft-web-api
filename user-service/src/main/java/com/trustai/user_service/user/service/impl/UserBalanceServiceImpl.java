package com.trustai.user_service.user.service.impl;

import com.trustai.user_service.user.repository.UserRepository;
import com.trustai.user_service.user.service.UserBalanceService;
import com.trustai.user_service.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserBalanceServiceImpl implements UserBalanceService {
    private final UserRepository userRepository;
    private final UserProfileService profileService;

    @Override
    public Optional<BigDecimal> findWalletBalanceById(Long userId) {
        //return userRepository.findWalletBalanceById(userId);
        var user = profileService.getUserById(userId);
        return Optional.ofNullable(user.getWalletBalance());
    }

    @Override
    public void updateWalletBalance(long userId, BigDecimal updatedAmount) {
        userRepository.updateWalletBalance(userId, updatedAmount);
    }

    @Override
    public Optional<BigDecimal> findDepositBalanceById(Long userId) {
        //return userRepository.findDepositBalanceById(userId);
        var user = profileService.getUserById(userId);
        return Optional.ofNullable(user.getDepositBalance());
    }

    @Override
    public void updateDepositBalance(long userId, BigDecimal updatedTotalDepositAmount) {
        userRepository.updateDepositBalance(userId, updatedTotalDepositAmount);
    }
}
