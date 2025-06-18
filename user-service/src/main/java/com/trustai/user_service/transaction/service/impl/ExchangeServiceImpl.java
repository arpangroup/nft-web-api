package com.trustai.user_service.transaction.service.impl;

import com.trustai.user_service.transaction.entity.Transaction;
import com.trustai.user_service.transaction.service.ExchangeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExchangeServiceImpl implements ExchangeService {
    @Override
    public Transaction exchange(long userId, BigDecimal fromAmount, String fromCurrency, BigDecimal toAmount, String toCurrency, String metaInfo) {
        return null;
    }
}
