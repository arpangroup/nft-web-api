package com.trustai.transaction_service.service;

import com.trustai.common.enums.TransactionType;
import com.trustai.transaction_service.entity.Transaction;

import java.math.BigDecimal;

public interface WalletService {
    BigDecimal getWalletBalance(Long userId);

    void updateBalanceFromTransaction(Long userId, BigDecimal delta);

    void ensureSufficientBalance(Long userId, BigDecimal amount);

    Transaction deduct(Long userId, BigDecimal amount, TransactionType transactionType, String remarks, String sourceModule);
    Transaction refund(Long userId, BigDecimal amount, TransactionType transactionType, String remarks, String sourceModule);
}
