package com.trustai.common.dto;

import com.trustai.common.enums.TransactionType;

import java.math.BigDecimal;

public record WalletRefundRequest(
        BigDecimal amount,
        String remarks,
        String sourceModule,
        TransactionType transactionType
) {}