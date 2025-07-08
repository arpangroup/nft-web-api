package com.trustai.transaction_service.entity;

import com.trustai.common.enums.PaymentGateway;
import com.trustai.common.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@Getter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = true)
    @Setter
    private Long senderId;

    @Column(nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = true)
    @Setter
    private String txnRefId; // should be unique

    @Column(nullable = false, updatable = false)
    private LocalDateTime txnDate;

    @Column(length = 255, nullable = true)
    @Setter
    private String remarks;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(nullable = true)
    @Setter
    private BigDecimal txnFee;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType txnType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private PaymentGateway gateway = PaymentGateway.SYSTEM;

    @Column(nullable = true)
    @Setter
    private String metaInfo;

    @Column(length = 3, nullable = false)
    private String currencyCode = "INR";

    @Column
    private Long linkedTxnId;// Useful for reversals, refunds, or when one transaction triggers another (e.g., commission, bonus, chargebacks).

    @Column(length = 50)
    private String sourceModule; // e.g., "investment", "referral", "marketplace" <== If transactions can originate from different modules

    @Column(nullable = false)
    private boolean isCredit;

    @Column(nullable = false)
    private boolean isDeleted = false; // Useful for compliance, data retention policies without deleting.

    @Column
    private String createdBy;

    @Column
    private String updatedBy;


    public Transaction(long userId, BigDecimal amount, @NotNull TransactionType transactionType, BigDecimal balance) {
        this.userId = userId;
        this.amount = amount;
        this.txnType = transactionType;
        this.balance = balance;
        this.txnDate = LocalDateTime.now();
    }

    public enum TransactionStatus {
        PENDING,
        SUCCESS,
        FAILED,
        CANCELLED;
    }
}
