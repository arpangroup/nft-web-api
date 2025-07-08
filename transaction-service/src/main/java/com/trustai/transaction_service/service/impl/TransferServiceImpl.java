package com.trustai.transaction_service.service.impl;

import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.service.TransferService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferServiceImpl implements TransferService {
    @Override
    public Transaction transferMoney(long senderId, long receiverId, BigDecimal amount, String message) {
        return null;
    }
}
