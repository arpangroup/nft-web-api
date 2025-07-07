package com.trustai.user_service.transaction.service.impl;

import com.trustai.common.enums.PaymentGateway;
import com.trustai.user_service.transaction.entity.Transaction;
import com.trustai.user_service.transaction.service.DepositService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class DepositServiceImpl implements DepositService {
    @Override
    public Transaction deposit(long userId, BigDecimal amount, PaymentGateway gateway, Optional<BigDecimal> txnFee, String txnRefId, Transaction.TransactionStatus status, String metaInfo) {
        return null;
    }

    @Override
    public Transaction depositManual(long userId, long depositor, BigDecimal amount, String remarks) {
        return null;
    }

    @Override
    public BigDecimal getTotalDeposit(long userId) {
        return null;
    }
}
