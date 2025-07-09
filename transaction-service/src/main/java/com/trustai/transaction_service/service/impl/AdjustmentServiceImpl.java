package com.trustai.transaction_service.service.impl;

import com.trustai.common.enums.PaymentGateway;
import com.trustai.common.enums.TransactionType;
import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.repository.TransactionRepository;
import com.trustai.transaction_service.service.AdjustmentService;
import com.trustai.transaction_service.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdjustmentServiceImpl implements AdjustmentService {
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;

    @Override
    public Transaction subtract(long userId, BigDecimal amount, String reason) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount to subtract must be greater than zero.");
        }

        walletService.ensureSufficientBalance(userId, amount);

        BigDecimal currentBalance = walletService.getWalletBalance(userId);
        BigDecimal newBalance = currentBalance.subtract(amount);

        Transaction txn = new Transaction(userId, amount, TransactionType.SUBTRACT, newBalance);
        txn.setGateway(PaymentGateway.SYSTEM);
        txn.setStatus(Transaction.TransactionStatus.SUCCESS);
        txn.setRemarks("Adjustment: " + reason);
        txn.setMetaInfo("manual_adjustment");

        transactionRepository.save(txn);
        walletService.updateBalanceFromTransaction(userId, amount.negate());

        return txn;
    }
}
