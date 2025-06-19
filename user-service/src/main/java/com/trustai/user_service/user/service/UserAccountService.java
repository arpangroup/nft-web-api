package com.trustai.user_service.user.service;

import com.trustai.user_service.user.entity.User;

public interface UserAccountService {
//    void activateAccount(Long userId);
//    void checkMinimumRequirements(User user);
//    boolean iasActive(Long userId); // consider renaming to `isActive`
//    User updateUserRank(Long userId, int newRank);

    User updateAccountStatus(Long userId, User.AccountStatus status);

    User updateTransactionStatus(Long userId, User.TransactionStatus depositStatus, User.TransactionStatus withdrawStatus, User.TransactionStatus sendMoneyStatus);
}
