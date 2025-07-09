package com.trustai.user_service.user.provider;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "transaction-service", url = "${transaction.service.url}", path = "/transactions")
public interface TransactionClient {
    @GetMapping("/balance/{userId}")
    BigDecimal getDepositBalance(@PathVariable Long userId);
}
