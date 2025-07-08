package com.trustai.transaction_service.service.impl;

import com.trustai.common.enums.PaymentGateway;
import com.trustai.common.enums.TransactionType;
import com.trustai.transaction_service.service.TransactionQueryService;
import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.repository.TransactionRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.trustai.common.enums.TransactionType.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionQueryServiceImpl implements TransactionQueryService {
    private final TransactionRepository transactionRepository;

    private final List<TransactionType> PROFIT_TYPES = List.of(SIGNUP_BONUS, REFERRAL, BONUS, INTEREST);

    @Override
    public Page<Transaction> getTransactions(Transaction.TransactionStatus status, Integer page, Integer size) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Transaction> transactionPage;
        if (status != null) {
            transactionPage = transactionRepository.findByStatus(status, pageable);
        } else {
            transactionPage = transactionRepository.findAll(pageable);
        }
        return transactionPage;
    }

    @Override
    public Page<Transaction> getProfits(Integer page, Integer size) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Transaction> transactionPage = transactionRepository.findByTxnTypeIn(PROFIT_TYPES, pageable);
        return transactionPage;
    }


    @Override
    public Page<Transaction> getTransactionsByUserId(Long userId, Pageable pageable) {
        return transactionRepository.findByUserId(userId, pageable);
    }

    @Override
    public Boolean hasDepositTransaction(Long userId) {
        return transactionRepository.existsByUserIdAndTxnType(userId, TransactionType.DEPOSIT);
    }

    @Override
    public Page<Transaction> getTransactionsByUserIdAndDateRange(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return transactionRepository.findByUserIdAndTxnDateBetween(userId, start, end, pageable);
    }

    @Override
    public Transaction findByTxnRefId(String txnRefId) {
        return transactionRepository.findByTxnRefId(txnRefId).orElse(null);
    }

    @Override
    public Page<Transaction> findTransfersBySenderId(Long senderId, Pageable pageable) {
        return transactionRepository.findBySenderId(senderId, pageable);
    }

    @Override
    public Page<Transaction> findTransfersByReceiverId(Long receiverId, Pageable pageable) {
        return transactionRepository.findByUserId(receiverId, pageable);
    }

    @Override
    public Page<Transaction> findByUserIdAndStatusAndGateway(Long userId, Transaction.TransactionStatus status, PaymentGateway gateway, Pageable pageable) {
        return transactionRepository.findByUserIdAndStatusAndGateway(userId, status, gateway, pageable);
    }

    @Override
    public Page<Transaction> searchTransactions(Long userId, Transaction.TransactionStatus status, TransactionType type,
                                                PaymentGateway gateway,
                                                LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {
        Specification<Transaction> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (userId != null) predicates.add(cb.equal(root.get("userId"), userId));
            if (status != null) predicates.add(cb.equal(root.get("status"), status));
            if (type != null) predicates.add(cb.equal(root.get("txnType"), type));
            if (gateway != null) predicates.add(cb.equal(root.get("gateway"), gateway));
            if (fromDate != null) predicates.add(cb.greaterThanOrEqualTo(root.get("txnDate"), fromDate));
            if (toDate != null) predicates.add(cb.lessThanOrEqualTo(root.get("txnDate"), toDate));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return transactionRepository.findAll(spec, pageable);
    }

    @Override
    public Page<Transaction> searchByMetaInfo(String keyword, Pageable pageable) {
        return transactionRepository.findByMetaInfoContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public Page<Transaction> searchByKeyword(Long userId, String keyword, Pageable pageable) {
        return transactionRepository.searchByUserIdAndKeyword(userId, keyword, pageable);
    }

    @Override
    public BigDecimal getTotalAmountByUserIdAndTxnType(Long userId, TransactionType txnType) {
        return transactionRepository.sumAmountByUserIdAndTxnType(userId, txnType);
    }

    @Override
    public List<Transaction> findTop10ByUserIdOrderByTxnDateDesc(Long userId) {
        return transactionRepository.findTop10ByUserIdOrderByTxnDateDesc(userId);
    }

    @Override
    public List<Transaction> findSuspiciousTransactions(BigDecimal threshold) {
        return transactionRepository.findByAmountGreaterThanEqual(threshold);
    }
}
