package com.trustai.transaction_service.service.impl;

import com.trustai.common.client.UserClient;
import com.trustai.common.enums.PaymentGateway;
import com.trustai.common.enums.TransactionType;
import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.exception.InsufficientBalanceException;
import com.trustai.transaction_service.repository.TransactionRepository;
import com.trustai.transaction_service.service.WalletService;
import com.trustai.transaction_service.util.TransactionIdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {
    private final TransactionRepository transactionRepository;
    private final UserClient userClient;

    /**
     * Returns the current wallet balance by summing all deposits and bonuses,
     * then subtracting all withdrawals, investments, and transfers sent.
     */
    @Override
    public BigDecimal getWalletBalance(Long userId) {
        log.debug("Retrieving wallet balance for userId: {}", userId);
        /*BigDecimal credits = transactionRepository.sumCredits(userId);
        BigDecimal debits = transactionRepository.sumDebits(userId);
        return credits.subtract(debits);*/
        return userClient.findWalletBalanceById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    @Transactional
    public void updateBalanceFromTransaction(Long userId, BigDecimal delta) {
        log.debug("Updating wallet balance for userId: {} with delta: {}", userId, delta);
        BigDecimal current = getWalletBalance(userId);
        BigDecimal updated = current.add(delta);
        userClient.updateWalletBalance(userId, updated);
        log.info("Wallet balance updated for userId: {}. Old Balance: {}, New Balance: {}", userId, current, updated);
    }


    @Override
    public void ensureSufficientBalance(Long userId, BigDecimal amount) {
        BigDecimal current = getWalletBalance(userId);
        log.debug("Checking if userId: {} has sufficient balance. Required: {}, Current: {}", userId, amount, current);
        if (current.compareTo(amount) < 0) {
            log.warn("Insufficient balance for userId: {}. Required: {}, Current: {}", userId, amount, current);
            throw new InsufficientBalanceException("Insufficient wallet balance");
        }
    }

    @Override
    @Transactional
    public Transaction deduct(Long userId, BigDecimal amount, TransactionType transactionType, String remarks, String sourceModule) {
        log.info("Starting deduction for userId: {}, amount: {}, type: {}, remarks: {}, source: {}", userId, amount, transactionType, remarks, sourceModule);
        ensureSufficientBalance(userId, amount);

        BigDecimal oldBalance = getWalletBalance(userId);
        BigDecimal newBalance = oldBalance.subtract(amount);

        Transaction txn = new Transaction(userId, amount, transactionType, newBalance, false);
        txn.setStatus(Transaction.TransactionStatus.SUCCESS);
        txn.setRemarks(remarks);
        txn.setSourceModule("investment"); // Or dynamically set by caller

        transactionRepository.save(txn);
        updateBalanceFromTransaction(userId, amount.negate()); // Update wallet balance

        log.info("Deduction complete for userId: {}. txnId: {}, amount: {}, newBalance: {}", userId, txn.getId(), amount, newBalance);
        return txn;
    }

    @Override
    @Transactional
    public Transaction refund(Long userId, BigDecimal amount, TransactionType transactionType, String remarks, String sourceModule) {
        log.info("Starting refund for userId: {}, amount: {}, type: {}, remarks: {}, source: {}", userId, amount, transactionType, remarks, sourceModule);

        // Load current balance
        BigDecimal currentBalance = getWalletBalance(userId);
        BigDecimal newBalance = currentBalance.add(amount);

        // Create Transaction
        Transaction txn = new Transaction(userId, amount, transactionType, newBalance, true);
        txn.setRemarks(remarks);
        txn.setStatus(Transaction.TransactionStatus.SUCCESS);
        txn.setGateway(PaymentGateway.SYSTEM);
        txn.setTxnRefId(TransactionIdGenerator.generateTransactionId());
        txn.setSourceModule(sourceModule);

        transactionRepository.save(txn);
        updateBalanceFromTransaction(userId, amount); // Update wallet

        log.info("Refund successful for userId: {}. txnId: {}, amount: {}, newBalance: {}", userId, txn.getId(), amount, newBalance);
        return txn;
    }

}
