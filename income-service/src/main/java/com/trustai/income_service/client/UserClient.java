package com.trustai.income_service.client;



import com.trustai.common.dto.UserInfo;

import java.math.BigDecimal;
import java.util.List;

public interface UserClient {
    UserInfo getUserInfo(long userId);
    List<UserInfo> getUserInfoByIds(List<Long> userIds);
    UserInfo getUserByReferralCode(String referralCode);
    Boolean hasDeposit(Long userId);
    BigDecimal getTotalDeposit(Long userId);
    UserInfo deposit(long userId, BigDecimal amount, String remarks, String metaInfo);
    UserInfo updateUserRank(long userId, int newRank);
}
