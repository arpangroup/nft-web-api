package com.trustai.user_service.user.service;


import com.trustai.user_service.user.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface UserService {
    User createUser(User user, String referralCode);
    User updateUser(Long userId, Map<String, Object> fieldsToUpdate);
    User updateUser(User user);
    User updateUserRank(Long userId, int newRank);

    List<User> getUsers();
    List<User> getUserByIds(List<Long> userIds);
    User getUserById(Long userId);
    User getUserByReferralCode(String referralCode);



    /**
     * This method is required for bonus-service to apply various bonus
     */
    User deposit(final long userId, final BigDecimal amount, String remarks, String metaInfo);

    /**
     * This method is require for BonusService to check whether
     * user has done his first deposit transaction or not.
     *  - if any deposit Txn exist, then apply the referral bonus
     *  - Otherwise any fake user can register and the referrer wil receive the referral-bonus
     *  - In order to receive the referral bonus user have to done at-least one deposit
     *  - Details logic is implemented in bonus-service
     */
    boolean hasDeposit(Long userId);
    BigDecimal getTotalDeposit(Long userId);

//    boolean iasActive(Long userId);
//    void handleDeposit(Long userId, double amount);
//    void activateAccount(Long userId);
//    void checkMinimumRequirements(User user);
//    void approveReferralBonus(Long userId);
}
