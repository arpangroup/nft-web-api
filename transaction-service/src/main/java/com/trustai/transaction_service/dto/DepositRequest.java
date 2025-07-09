package com.trustai.transaction_service.dto;

import com.trustai.common.enums.PaymentGateway;

import java.math.BigDecimal;

public record DepositRequest(
        Long userId,
        BigDecimal amount,
        PaymentGateway gateway,
        BigDecimal txnFee,// nullable
        String txnRefId,
        String metaInfo
) {}