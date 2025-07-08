package com.trustai.transaction_service.service.impl;

import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.service.RefundService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RefundServiceImpl implements RefundService {
    @Override
    public Transaction refund(long userId, BigDecimal amount, String originalTxnRef, String reason) {
        return null;
    }
}
