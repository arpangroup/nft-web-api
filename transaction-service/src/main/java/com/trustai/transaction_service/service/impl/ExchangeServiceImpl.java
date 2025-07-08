package com.trustai.transaction_service.service.impl;

import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.service.ExchangeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExchangeServiceImpl implements ExchangeService {
    @Override
    public Transaction exchange(long userId, BigDecimal fromAmount, String fromCurrency, BigDecimal toAmount, String toCurrency, String metaInfo) {
        return null;
    }
}
