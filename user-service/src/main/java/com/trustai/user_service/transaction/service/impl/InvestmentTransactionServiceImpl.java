package com.trustai.user_service.transaction.service.impl;

import com.trustai.user_service.transaction.entity.Transaction;
import com.trustai.user_service.transaction.service.InvestmentTransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InvestmentTransactionServiceImpl implements InvestmentTransactionService {
    @Override
    public Transaction invest(long userId, BigDecimal amount, String investmentType, String metaInfo) {
        return null;
    }
}
