package com.trustai.transaction_service.service.impl;

import com.trustai.common.client.UserClient;
import com.trustai.transaction_service.repository.TransactionRepository;
import com.trustai.transaction_service.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final TransactionRepository transactionRepository;
    private final UserClient userClient;

    /**
     * Returns the current wallet balance by summing all deposits and bonuses,
     * then subtracting all withdrawals, investments, and transfers sent.
     */
    @Override
    public BigDecimal getUserBalance(Long userId) {
        /*BigDecimal credits = transactionRepository.sumCredits(userId);
        BigDecimal debits = transactionRepository.sumDebits(userId);
        return credits.subtract(debits);*/
        return userClient.findWalletBalanceById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public void updateBalanceFromTransaction(Long userId, BigDecimal delta) {
        BigDecimal current = getUserBalance(userId);
        BigDecimal updated = current.add(delta);
        userClient.updateWalletBalance(userId, updated);
    }

    @Override
    public void ensureSufficientBalance(Long userId, BigDecimal amount) {
        BigDecimal balance = getUserBalance(userId);
        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient wallet balance");
        }
    }
}
