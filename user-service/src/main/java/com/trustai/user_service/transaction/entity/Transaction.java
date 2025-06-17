package com.trustai.user_service.transaction.entity;

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

    @Column(nullable = true)
    @Setter
    private String status;

    @Column(nullable = true)
    @Setter
    private BigDecimal txnFee;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType txnType;

    @Column(nullable = true)
    @Setter
    private String metaInfo;

    public Transaction(long userId, BigDecimal amount, @NotNull TransactionType transactionType, BigDecimal balance) {
        this.userId = userId;
        this.amount = amount;
        this.txnType = transactionType;
        this.balance = balance;
        this.txnDate = LocalDateTime.now();
    }

    public static enum TransactionStatus {
        PENDING,
        COMPLETED,
        FAILED,
        CANCELLED
    }
}
