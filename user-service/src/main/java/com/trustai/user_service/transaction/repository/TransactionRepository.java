package com.trustai.user_service.transaction.repository;

import com.trustai.common.enums.TransactionType;
import com.trustai.user_service.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>  {
    Page<Transaction> findByStatus(Transaction.TransactionStatus status, Pageable pageable);
    Page<Transaction> findByTxnTypeIn(List<TransactionType> txnTypes, Pageable pageable);
}
