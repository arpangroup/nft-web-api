package com.trustai.user_service.transaction.service.impl;

import com.trustai.user_service.transaction.entity.Transaction;
import com.trustai.user_service.transaction.service.TransferService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferServiceImpl implements TransferService {
    @Override
    public Transaction transferMoney(long senderId, long receiverId, BigDecimal amount, String message) {
        return null;
    }
}
