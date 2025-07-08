package com.trustai.transaction_service.service.impl;

import com.trustai.common.client.UserClient;
import com.trustai.common.enums.PaymentGateway;
import com.trustai.common.enums.TransactionType;
import com.trustai.transaction_service.dto.DepositRequest;
import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.repository.TransactionRepository;
import com.trustai.transaction_service.service.DepositService;
import com.trustai.transaction_service.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.pqc.jcajce.provider.NTRU;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class DepositServiceImpl implements DepositService {
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final UserClient userClient;

    @Override
    @Transactional
    public Transaction deposit(@NonNull DepositRequest request) {
        validateDepositRequest(request);

        BigDecimal fee = Optional.ofNullable(request.txnFee()).orElse(BigDecimal.ZERO);
        BigDecimal netAmount = request.amount().subtract(fee);

        return createAndSaveTransaction(
                request.userId(),
                request.amount(),
                netAmount,
                request.gateway(),
                TransactionType.DEPOSIT,
                Transaction.TransactionStatus.SUCCESS,
                request.txnRefId(),
                fee,
                "Deposit via " + request.gateway().name(),
                request.metaInfo(),
                null // no sender for user-initiated
        );
    }

    @Override
    @Transactional
    public Transaction depositManual(long userId, long depositor, BigDecimal amount, String remarks) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero for manual deposit.");
        }

        return createAndSaveTransaction(
                userId,
                amount,
                amount, // no fee in manual
                PaymentGateway.SYSTEM,
                TransactionType.DEPOSIT_MANUAL,
                Transaction.TransactionStatus.SUCCESS,
                null,  // no txnRefId
                null,
                remarks != null ? remarks : "Manual deposit by admin",
                "manual_deposit",
                depositor
        );
    }

    @Override
    public BigDecimal getTotalDeposit(long userId) {
        return transactionRepository.sumAmountByUserIdAndTxnType(userId, TransactionType.DEPOSIT);
    }

    @Override
    public boolean isDepositExistsByTxnRef(String txnRefId) {
        return transactionRepository.findByTxnRefId(txnRefId).isPresent();
    }

    @Override
    public Page<Transaction> getDepositHistory(Long userId, Pageable pageable) {
        return transactionRepository.findByUserIdAndTxnType(userId, TransactionType.DEPOSIT, pageable);
    }


    @Override
    public Transaction confirmGatewayDeposit(String txnRefId, String gatewayResponseJson) {
        Optional<Transaction> optional = transactionRepository.findByTxnRefId(txnRefId);
        if (optional.isPresent()) {
            Transaction txn = optional.get();
            txn.setStatus(Transaction.TransactionStatus.SUCCESS);
            txn.setMetaInfo(gatewayResponseJson);
            return transactionRepository.save(txn);
        }
        throw new IllegalArgumentException("Transaction not found with reference: " + txnRefId);
    }


    private void validateDepositRequest(DepositRequest request) {
        if (request.userId() == null || request.userId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        if (request.gateway() == null) {
            throw new IllegalArgumentException("Payment gateway must be provided");
        }
        if (request.txnRefId() == null || request.txnRefId().isBlank()) {
            throw new IllegalArgumentException("Transaction reference ID is required");
        }
        if (request.txnFee() != null && request.txnFee().compareTo(request.amount()) > 0) {
            throw new IllegalArgumentException("Transaction fee cannot exceed the deposit amount");
        }
    }

    private Transaction createAndSaveTransaction(
            Long userId,
            BigDecimal grossAmount,
            BigDecimal netAmount,
            PaymentGateway gateway,
            TransactionType txnType,
            Transaction.TransactionStatus status,
            String txnRefId,
            BigDecimal txnFee,
            String remarks,
            String metaInfo,
            Long senderId
    ) {
        BigDecimal currentBalance = walletService.getUserBalance(userId);
        BigDecimal newBalance = currentBalance.add(netAmount);

        Transaction transaction = new Transaction(userId, grossAmount, txnType, newBalance);

        transaction.setTxnRefId(txnRefId);
        transaction.setTxnFee(txnFee);
        transaction.setGateway(gateway);
        transaction.setStatus(status);
        transaction.setRemarks(remarks);
        transaction.setMetaInfo(metaInfo);
        transaction.setSenderId(senderId);

        transactionRepository.save(transaction);
        walletService.updateBalanceFromTransaction(userId, netAmount, TransactionType.DEPOSIT);

        return transaction;
    }
}
