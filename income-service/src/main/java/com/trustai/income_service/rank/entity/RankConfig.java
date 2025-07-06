package com.trustai.income_service.rank.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "config_rank")
@NoArgsConstructor
@Data
public class RankConfig {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "rank_type") // renamed to avoid reserved keyword
    private Rank rank; // RANK_1, RANK_2, etc.

    private int minWalletBalance;
    private int maxWalletBalance;
    private int txnPerDay = 1;
    //private double profitPerDay;
    private int stakeValue;

    // Required downline users per level (depth = 1 = level A, etc.)
    @ElementCollection //  Tells JPA this is a collection of simple values (not entities).
    @CollectionTable(name = "rank_required_downlines", joinColumns = @JoinColumn(name = "rank_type")) // Specifies a separate table to store the map.
    @MapKeyColumn(name = "depth") // The key of the map will be stored as depth (e.g., 1 = level A, 2 = level B...).
    @Column(name = "required_count") // The value of the map — how many users are required at that depth.
    private Map<Integer, Integer> requiredLevelCounts = new HashMap<>();

    // Optional fields like commission rate, etc. can go here
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal commissionRate = BigDecimal.ZERO;

    public RankConfig(Rank rank, int minWalletBalance, int maxWalletBalance) {
        this.rank = rank;
        this.minWalletBalance = minWalletBalance;
        this.maxWalletBalance = maxWalletBalance;
    }

    public RankConfig(Rank rank, int minWalletBalance, int maxWalletBalance, BigDecimal commissionRate) {
       this(rank, minWalletBalance, maxWalletBalance);
       this.commissionRate = commissionRate;
       this.txnPerDay = 1;
    }
}
