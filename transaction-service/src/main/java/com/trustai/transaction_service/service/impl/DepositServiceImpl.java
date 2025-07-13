package com.trustai.transaction_service.service.impl;

import com.trustai.common.client.UserClient;
import com.trustai.common.enums.PaymentGateway;
import com.trustai.common.enums.TransactionType;
import com.trustai.transaction_service.dto.response.DepositHistoryItem;
import com.trustai.transaction_service.dto.request.DepositRequest;
import com.trustai.transaction_service.dto.request.ManualDepositRequest;
import com.trustai.transaction_service.entity.PendingDeposit;
import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.mapper.TransactionMapper;
import com.trustai.transaction_service.repository.PendingDepositRepository;
import com.trustai.transaction_service.repository.TransactionRepository;
import com.trustai.transaction_service.service.DepositService;
import com.trustai.transaction_service.service.WalletService;
import com.trustai.transaction_service.util.TransactionIdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class DepositServiceImpl implements DepositService {
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final UserClient userClient;
    private final PendingDepositRepository pendingDepositRepository;
    private final TransactionMapper mapper;
    private final List<TransactionType> DEPOSIT_TRANSACTIONS = List.of(TransactionType.DEPOSIT, TransactionType.DEPOSIT_MANUAL);

    @Override
    @Transactional
    public PendingDeposit depositManual(ManualDepositRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero for manual deposit.");
        }

        PendingDeposit deposit = buildPendingDeposit(
                request.getUserId(),
                request.getAmount(),
                "", // txnRefId empty for manual
                BigDecimal.ZERO,
                PaymentGateway.SYSTEM,
                request.getRemarks(),
                request.getMetaInfo(),
                "INR", // or make this configurable
                PendingDeposit.DepositStatus.PENDING,
                request.getLinkedAccountId() // linkedAccountId is required to identify from which account the txn happened (eg: upiID)
        );;
        pendingDepositRepository.save(deposit);
        log.info("Manual PendingDeposit created with ID: {} and status: {}", deposit.getId(), deposit.getStatus());

        return deposit;
    }

    @Override
    @Transactional
    public PendingDeposit deposit(@NonNull DepositRequest request) {
        log.info("Processing deposit for userId: {}, amount: {}", request.getUserId(), request.getAmount());
        validateDepositRequest(request);

        PaymentGateway paymentGateway = PaymentGateway.valueOf(request.getPaymentGateway());
        BigDecimal fee = calculateTxnFee(paymentGateway, request.getAmount());
        BigDecimal netAmount = request.getAmount().subtract(fee);
        log.debug("Calculated fee: {}, netAmount: {}", fee, netAmount);


        PendingDeposit deposit = buildPendingDeposit(
                request.getUserId(),
                request.getAmount(),
                request.getTxnRefId(),
                fee,
                paymentGateway,
                request.getRemarks(),
                request.getMetaInfo(),
                request.getCurrencyCode(),
                PendingDeposit.DepositStatus.APPROVED,
                null // no linkedTxnId
        );
        pendingDepositRepository.save(deposit);
        log.info("PendingDeposit created successfully with ID: {} and status: {}", deposit.getId(), deposit.getStatus());

        Transaction transaction = createAndSaveTransaction(
                request.getUserId(),
                request.getAmount(),
                netAmount,
                paymentGateway,
                TransactionType.DEPOSIT,
                Transaction.TransactionStatus.SUCCESS,
                request.getTxnRefId(),
                fee,
                null, // no need of linkedTxnId as the txn is direct vis PaymentGateway, we can track via txnRefId and the gateway
                "Deposit via " + paymentGateway.name(),
                request.getMetaInfo(),
                null // no sender for user-initiated
        );
        log.info("Deposit transaction created successfully with ID: {}", transaction.getId());

        return deposit;
    }

    @Override
    @Transactional
    public PendingDeposit approvePendingDeposit(Long depositId, String adminUser) {
        PendingDeposit deposit = pendingDepositRepository.findById(depositId)
                .orElseThrow(() -> new IllegalArgumentException("PendingDeposit not found"));

        if (deposit.getStatus() != PendingDeposit.DepositStatus.PENDING) {
            throw new IllegalStateException("Only pending deposits can be approved.");
        }

        BigDecimal netAmount = deposit.getAmount().subtract(deposit.getTxnFee());

        Transaction transaction = createAndSaveTransaction(
                deposit.getUserId(),
                deposit.getAmount(),
                netAmount,
                deposit.getGateway(),
                TransactionType.DEPOSIT,
                Transaction.TransactionStatus.SUCCESS,
                deposit.getTxnRefId(),
                deposit.getTxnFee(),
                deposit.getLinkedTxnId(),
                "Manual deposit approved",
                deposit.getMetaInfo(),
                null
        );

        deposit.setStatus(PendingDeposit.DepositStatus.APPROVED);
        deposit.setApprovedBy(adminUser);
        deposit.setApprovedAt(LocalDateTime.now());
        deposit.setLinkedTxnId(transaction.getId().toString()); // link to created txn

        return pendingDepositRepository.save(deposit);
    }

    @Override
    @Transactional
    public PendingDeposit rejectPendingDeposit(Long depositId, String adminUser, String reason) {
        PendingDeposit deposit = pendingDepositRepository.findById(depositId)
                .orElseThrow(() -> new IllegalArgumentException("PendingDeposit not found"));

        if (deposit.getStatus() != PendingDeposit.DepositStatus.PENDING) {
            throw new IllegalStateException("Only pending deposits can be rejected.");
        }

        deposit.setStatus(PendingDeposit.DepositStatus.REJECTED);
        deposit.setRejectedBy(adminUser);
        deposit.setRejectedAt(LocalDateTime.now());
        deposit.setRejectionReason(reason);

        return pendingDepositRepository.save(deposit);
    }

    @Override
    public BigDecimal getTotalDeposit(long userId) {
        BigDecimal total = transactionRepository.sumAmountByUserIdAndTxnTypeAndStatusIn(
                userId,
                List.of(TransactionType.DEPOSIT, TransactionType.DEPOSIT_MANUAL),
                List.of(Transaction.TransactionStatus.SUCCESS)
        );
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public boolean isDepositExistsByTxnRef(String txnRefId) {
        return transactionRepository.findByTxnRefId(txnRefId).isPresent();
    }

    @Override
    public Page<DepositHistoryItem> getDepositHistory(Long userId, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));

        Page<Transaction> transactions = transactionRepository.findByUserIdAndTxnType(userId, TransactionType.DEPOSIT, pageable);
        return transactions.map(mapper::mapToDepositHistory);
    }

    @Override
    public Page<DepositHistoryItem> getDepositHistory(Transaction.TransactionStatus status, Pageable pageable) {
        log.debug("Fetching deposit history with status: {}, page request: {}", status, pageable);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));

        if (status == Transaction.TransactionStatus.PENDING) {
            Page<PendingDeposit> pendingDeposits = pendingDepositRepository.findByStatus(PendingDeposit.DepositStatus.PENDING, pageable);
            return pendingDeposits.map(mapper::mapToDepositHistory);
        } else if (status == null) {
            Page<Transaction> transactions = transactionRepository.findByTxnTypeIn(DEPOSIT_TRANSACTIONS, pageable);
            return transactions.map(mapper::mapToDepositHistory);
        } else {
            Page<Transaction> transactions = transactionRepository.findByTxnTypeAndStatus(TransactionType.DEPOSIT, status, pageable);
            return transactions.map(mapper::mapToDepositHistory);
        }
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
        if (request.getUserId() == null || request.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        /*if (request.getPaymentGateway() == null) {
            throw new IllegalArgumentException("Payment paymentGateway must be provided");
        }
        if (request.getTxnRefId() == null || request.getTxnRefId().isBlank()) {
            throw new IllegalArgumentException("Transaction reference ID is required");
        }*/
    }

    private BigDecimal calculateTxnFee(PaymentGateway paymentGateway, BigDecimal txnAmount) {
        return BigDecimal.ZERO;
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
            String linkedTxnId,
            String remarks,
            String metaInfo,
            Long senderId
    ) {
        BigDecimal currentBalance = walletService.getWalletBalance(userId);
        BigDecimal newBalance = currentBalance.add(netAmount);

        Transaction txn = new Transaction(userId, grossAmount, txnType, newBalance);

        txn.setTxnFee(txnFee);
        txn.setLinkedTxnId(linkedTxnId);
        txn.setGateway(gateway);
        txn.setStatus(status);
        txn.setRemarks(remarks);
        txn.setMetaInfo(metaInfo);
        txn.setSenderId(senderId);

        if (txnRefId == null) {
            txnRefId = TransactionIdGenerator.generateTransactionId();
            txn.setTxnRefId(txnRefId);
        }

        transactionRepository.save(txn);
        walletService.updateBalanceFromTransaction(userId, netAmount, TransactionType.DEPOSIT);

        return txn;
    }

    private PendingDeposit buildPendingDeposit(
            long userId,
            BigDecimal amount,
            String txnRefId,
            BigDecimal txnFee,
            PaymentGateway gateway,
            String remarks,
            String metaInfo,
            String currencyCode,
            PendingDeposit.DepositStatus status,
            String linkedTxnId
    ) {
        if (txnRefId == null) txnRefId = TransactionIdGenerator.generateTransactionId();
        return new PendingDeposit(userId, amount)
                .setTxnRefId(txnRefId)
                .setTxnFee(txnFee)
                .setGateway(gateway)
                .setRemarks(remarks)
                .setMetaInfo(metaInfo)
                .setCurrencyCode(currencyCode)
                .setStatus(status)
                .setLinkedTxnId(linkedTxnId);
    }

}
