package com.trustai.transaction_service.service.impl;

import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.service.AdjustmentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AdjustmentServiceImpl implements AdjustmentService {
    @Override
    public Transaction subtract(long userId, BigDecimal amount, String reason) {
        return null;
    }
}
