package com.trustai.user_service.transaction.service.impl;

import com.trustai.user_service.transaction.entity.Transaction;
import com.trustai.user_service.transaction.service.WithdrawalService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {
    @Override
    public Transaction withdraw(long userId, BigDecimal amount, String destinationAccount, String remarks) {
        return null;
    }
}
