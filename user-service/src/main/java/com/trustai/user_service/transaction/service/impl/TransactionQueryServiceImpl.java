package com.trustai.user_service.transaction.service.impl;

import com.trustai.user_service.transaction.entity.Transaction;
import com.trustai.user_service.transaction.service.TransactionQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionQueryServiceImpl implements TransactionQueryService {
    @Override
    public Page<Transaction> getTransactions(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Transaction> getTransactionsByUserId(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public Boolean hasDepositTransaction(Long userId) {
        return null;
    }
}
