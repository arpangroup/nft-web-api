package com.trustai.transaction_service.controller;

import com.trustai.common.dto.WalletDeductRequest;
import com.trustai.common.dto.WalletRefundRequest;
import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
@Slf4j
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> getWalletBalance(@PathVariable Long userId) {
        log.info("Fetching wallet balance for userId: {}", userId);
        return ResponseEntity.ok(walletService.getWalletBalance(userId));
    }

    @PostMapping("/{userId}/deduct")
    public ResponseEntity<Transaction> deduct(@PathVariable Long userId, @RequestBody WalletDeductRequest request) {
        log.info("Initiating wallet deduction for userId: {}, amount: {}, type: {}, remarks: {}, source: {}", userId, request.amount(), request.transactionType(), request.remarks(), request.sourceModule());
        Transaction txn = walletService.deduct(userId, request.amount(), request.transactionType(), request.remarks(), request.sourceModule());
        log.info("Wallet deduction successful for userId: {}, txnId: {}, newBalance: {}", userId, txn.getId(), txn.getBalance());
        return ResponseEntity.ok(txn);
    }

    @PostMapping("/{userId}/refund")
    public ResponseEntity<Transaction> refund(@PathVariable Long userId, @RequestBody WalletRefundRequest request) {
        log.info("Initiating wallet refund for userId: {}, amount: {}, type: {}, remarks: {}, source: {}", userId, request.amount(), request.transactionType(), request.remarks(), request.sourceModule());
        Transaction txn = walletService.refund(userId, request.amount(), request.transactionType(), request.remarks(), request.sourceModule());
        log.info("Wallet refund successful for userId: {}, txnId: {}, newBalance: {}", userId, txn.getId(), txn.getBalance());
        return ResponseEntity.ok(txn);
    }
}
