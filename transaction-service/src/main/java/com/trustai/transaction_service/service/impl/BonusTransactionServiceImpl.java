package com.trustai.transaction_service.service.impl;

import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.service.BonusTransactionService;
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
