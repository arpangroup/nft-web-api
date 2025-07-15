package com.trustai.investment_service.provider;

import com.trustai.common.config.FeignConfig;
import com.trustai.common.dto.TransactionDto;
import com.trustai.common.dto.WalletDeductRequest;
import com.trustai.common.dto.WalletRefundRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient(
        name = "wallet-service",
        url = "http://localhost:8080",
        path = "/api/v1/wallet",
        configuration = FeignConfig.class
)
public interface WalletClient {
    @GetMapping("/{userId}/balance")
    BigDecimal getWalletBalance(@PathVariable("userId") Long userId);

    @PostMapping("/{userId}/deduct")
    TransactionDto deduct(
            @PathVariable("userId") Long userId,
            @RequestBody WalletDeductRequest request
    );

    @PostMapping("/{userId}/refund")
    TransactionDto refund(
            @PathVariable("userId") Long userId,
            @RequestBody WalletRefundRequest request
    );


}
