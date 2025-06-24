package com.trustai.income_service.client;

import com.trustai.common.dto.UserInfo;
import com.trustai.user_service.user.mapper.UserMapper;
import com.trustai.user_service.user.service.UserAccountService;
import com.trustai.user_service.user.service.UserReferralService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
//@ConditionalOnProperty(name = "user.provider.type", havingValue = "local")
@RequiredArgsConstructor
@Slf4j
public class LocalUserClient implements UserClient {
    private final UserReferralService userReferralService;
    private final UserAccountService userAccountService;
    private final UserMapper mapper;

    @Override
    public UserInfo getUserInfo(long userId) {
        log.info("getUserById: {}", userId);
        return mapper.mapTo(userReferralService.getUserById(userId));
    }

    @Override
    public List<UserInfo> getUserInfoByIds(List<Long> userIds) {
        log.info("getUserByIds: {}", userIds);
        return userReferralService.getUserByIds(userIds).stream().map(mapper::mapTo).toList();
    }

    @Override
    public UserInfo getUserByReferralCode(String referralCode) {
        log.info("getUserByReferralCode: {}", referralCode);
        return mapper.mapTo(userReferralService.getUserByReferralCode(referralCode));
    }

    @Override
    public Boolean hasDeposit(Long userId) {
        log.info("hasDeposit for userId: {}", userId);
        //return userAccountService.hasDeposit(userId);
        return true;
    }

    @Override
    public BigDecimal getTotalDeposit(Long userId) {
        log.info("hasDeposit for userId: {}", userId);
        //return userReferralService.getTotalDeposit(userId);
        return BigDecimal.ZERO;
    }

    @Override
    public UserInfo deposit(long userId, BigDecimal amount, String remarks,  String metaInfo) {
        log.info("deposit for userId: {}, amount: {}, remarks: {}", userId, amount, remarks);
        //return mapper.mapTo(userReferralService.deposit(userId, amount, remarks, metaInfo));
        return null;
    }

    @Override
    public UserInfo updateUserRank(long userId, int newRank) {
        log.info("updateUserRank for userId: {}, newRank: {}", userId, newRank);
        //return mapper.mapTo(userReferralService.updateUserRank(userId, newRank));
        return null;
    }
}
