package com.trustai.transaction_service.service.impl;

import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.service.WithdrawalService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {
    @Override
    public Transaction withdraw(long userId, BigDecimal amount, String destinationAccount, String remarks) {
        return null;
    }
}
