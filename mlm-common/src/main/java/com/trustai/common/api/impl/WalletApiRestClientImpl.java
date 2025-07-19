package com.trustai.common.api.impl;

import com.trustai.common.api.WalletApi;
import com.trustai.common.dto.TransactionDto;
import com.trustai.common.dto.WalletUpdateRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
//@ConditionalOnProperty(name = "user.api.rest.enabled", havingValue = "true", matchIfMissing = true)
public class WalletApiRestClientImpl implements WalletApi {
    @Override
    public BigDecimal getWalletBalance(Long userId) {
        return null;
    }

    @Override
    public TransactionDto updateWalletBalance(Long userId, WalletUpdateRequest request) {
        return null;
    }
}
