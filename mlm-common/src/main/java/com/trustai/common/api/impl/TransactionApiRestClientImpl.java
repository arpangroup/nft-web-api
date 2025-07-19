package com.trustai.common.api.impl;

import com.trustai.common.api.TransactionApi;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionApiRestClientImpl implements TransactionApi {
    @Override
    public BigDecimal getDepositBalance(Long userId) {
        return null;
    }
}
