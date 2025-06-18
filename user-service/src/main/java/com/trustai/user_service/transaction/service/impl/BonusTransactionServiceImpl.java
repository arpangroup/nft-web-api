package com.trustai.user_service.transaction.service.impl;

import com.trustai.user_service.transaction.entity.Transaction;
import com.trustai.user_service.transaction.service.BonusTransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BonusTransactionServiceImpl implements BonusTransactionService {
    @Override
    public Transaction applySignupBonus(long userId, BigDecimal bonusAmount) {
        return null;
    }

    @Override
    public Transaction applyReferralBonus(long referrerUserId, long referredUserId, BigDecimal bonusAmount) {
        return null;
    }

    @Override
    public Transaction applyBonus(long userId, BigDecimal bonusAmount, String reason) {
        return null;
    }

    @Override
    public Transaction applyInterest(long userId, BigDecimal interestAmount, String periodDescription) {
        return null;
    }
}
