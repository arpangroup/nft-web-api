package com.trustai.transaction_service.dto;

import com.trustai.common.enums.PaymentGateway;

import java.math.BigDecimal;

public record DepositRequest(
        Long userId,
        BigDecimal amount,
        PaymentGateway gateway,
        String txnRefId,
        BigDecimal txnFee,// nullable
        String metaInfo
) {}