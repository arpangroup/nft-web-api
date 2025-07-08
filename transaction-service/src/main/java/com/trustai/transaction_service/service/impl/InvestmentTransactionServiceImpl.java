package com.trustai.transaction_service.service.impl;

import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.service.InvestmentTransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InvestmentTransactionServiceImpl implements InvestmentTransactionService {
    @Override
    public Transaction invest(long userId, BigDecimal amount, String investmentType, String metaInfo) {
        return null;
    }
}
