package com.trustai.user_service.transaction.service;

import com.trustai.user_service.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionQueryService {
    Page<Transaction> getTransactions(Pageable pageable);
    Page<Transaction> getTransactionsByUserId(Long userId, Pageable pageable);
    Boolean hasDepositTransaction(Long userId);
}
