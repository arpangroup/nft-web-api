package com.trustai.user_service.user.service;

import com.trustai.user_service.user.entity.User;

import java.util.List;

public interface UserReferralService {
    List<User> getUserById(Long userId);
    List<User> getUserByIds(List<Long> userIds);
    User getUserByReferralCode(String referralCode);
    void approveReferralBonus(Long userId);
}
