package com.trustai.user_service.transaction.service.impl;

import com.trustai.user_service.transaction.entity.Transaction;
import com.trustai.user_service.transaction.service.AdjustmentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AdjustmentServiceImpl implements AdjustmentService {
    @Override
    public Transaction subtract(long userId, BigDecimal amount, String reason) {
        return null;
    }
}
