package com.trustai.transaction_service.mapper;

import com.trustai.transaction_service.dto.response.DepositHistoryItem;
import com.trustai.transaction_service.entity.PendingDeposit;
import com.trustai.transaction_service.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public DepositHistoryItem mapToDepositHistory(Transaction transaction) {
        return DepositHistoryItem.builder()
                .id(transaction.getId())
                .txnRefId(transaction.getTxnRefId())
                .amount(transaction.getAmount())
                .linkedAccountId(transaction.getSenderId() != null ? transaction.getSenderId().toString() : null)
                .paymentGateway(transaction.getGateway().name())
                .currencyCode(transaction.getCurrencyCode())
                .status(transaction.getStatus().name())
                .remarks(transaction.getRemarks())
                .build();
    }

    public DepositHistoryItem mapToDepositHistory(PendingDeposit deposit) {
        return DepositHistoryItem.builder()
                .id(deposit.getId())
                .txnRefId(deposit.getTxnRefId())
                .amount(deposit.getAmount())
                .linkedAccountId(deposit.getLinkedTxnId())
                .paymentGateway(deposit.getGateway().name())
                .currencyCode(deposit.getCurrencyCode())
                .status(deposit.getStatus().name())
                .remarks(deposit.getRemarks())
                .build();
    }
}
