package com.trustai.user_service.transaction.service.impl;

import com.trustai.user_service.transaction.entity.Transaction;
import com.trustai.user_service.transaction.service.RefundService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RefundServiceImpl implements RefundService {
    @Override
    public Transaction refund(long userId, BigDecimal amount, String originalTxnRef, String reason) {
        return null;
    }
}
