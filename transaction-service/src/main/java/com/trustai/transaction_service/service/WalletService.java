package com.trustai.transaction_service.service;

import com.trustai.common.enums.TransactionType;

import java.math.BigDecimal;

public interface WalletService {
    BigDecimal getWalletBalance(Long userId);
    void updateBalanceFromTransaction(Long userId, BigDecimal delta);
    void updateBalanceFromTransaction(Long userId, BigDecimal delta, TransactionType transactionType);
    void ensureSufficientBalance(Long userId, BigDecimal amount);
}
