package com.trustai.user_service.transaction.service.impl;

import com.trustai.common.dto.UserInfo;
import com.trustai.common.enums.TransactionType;
import com.trustai.user_service.transaction.entity.Transaction;
import com.trustai.user_service.transaction.repository.TransactionRepository;
import com.trustai.user_service.transaction.service.TransactionQueryService;
import com.trustai.user_service.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public Boolean hasDepositTransaction(Long userId) {
        return null;
    }
}
