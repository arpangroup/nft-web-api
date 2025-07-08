package com.trustai.transaction_service.service;

import java.math.BigDecimal;

public interface WalletService {
    BigDecimal getUserBalance(Long userId);
    void updateBalanceFromTransaction(Long userId, BigDecimal delta);
    void ensureSufficientBalance(Long userId, BigDecimal amount);
}
