package com.trustai.income_service.rank.entity;

/*
| Field        | Type      | Purpose                                |
| ------------ | --------- | -------------------------------------- |
| `priority`   | `int`     | Order of rank evaluation               |
| `achievable` | `boolean` | Can user reach this rank currently?    |
| `imageUrl`   | `String`  | Icon/logo for frontend display         |
| `rewardType` | `enum`    | E.g., CASH, PRODUCT, POINTS            |
| `rankType`   | `enum`    | E.g., PERFORMANCE, STATIC, PROMOTIONAL |

 */

/*
Required Fields:
| Field                        | Description                                                      | Usage in Rank Evaluation                                                      |
| ---------------------------- | ---------------------------------------------------------------- | ----------------------------------------------------------------------------- |
| `minDepositAmount`           | Minimum personal deposit by user                                 | Ensures user has invested some funds personally (used in crypto/finance MLMs) |
| `minInvestmentAmount`        | Minimum personal investment in a product or plan                 | Used to promote high-tier plan purchases before rank eligibility              |
| `minDirectReferrals`         | Number of **directly referred active users** required            | Enforces direct engagement, not just team building                            |
| `minReferralTotalDeposit`    | Total deposit from **all direct referrals**                      | Ensures referred users are active/investing                                   |
| `minReferralTotalInvestment` | Total investment of referred users (could include indirect team) | Used to evaluate the quality and depth of referral impact                     |
| `minTotalEarnings`           | User's cumulative earnings in the system                         | Useful to restrict high ranks to genuinely earning users                      |
| `commissionPercentage`       | How much commission (%) this rank earns (e.g., in team income)   | Determines income multiplier/bonus per event                                  |
| `rankBonus`                  | One-time bonus on achieving this rank                            | Often shown in wallets or history as milestone reward                         |

 */

/*
Optional / Advanced Control Fields:
| Field                 | Description                                                 | Usage                                                            |
| --------------------- | ----------------------------------------------------------- | ---------------------------------------------------------------- |
| `achievable`          | Can this rank currently be achieved by users (`true/false`) | Useful for marketing campaigns or limited-time ranks             |
| `rewardType` (`enum`) | Type of reward: `CASH`, `PRODUCT`, `VOUCHER`, `TOKEN`, etc. | Tells system how to issue rank bonus                             |
| `rankType` (`enum`)   | Type of rank: `STATIC`, `PERFORMANCE`, `PROMOTIONAL`, etc.  | Allows multiple rank systems (e.g., fixed vs time-limited ranks) |

 */

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "rankings")
@NoArgsConstructor
@Data
public class RankConfigV2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // RANK_1, RANK_2
    private String displayName; // e.g., Bronze, Silver, Gold
    private int priority; // Order of rank evaluation

    @Column(precision = 19, scale = 4)
    private BigDecimal minDepositAmount = BigDecimal.ZERO;
    @Column(precision = 19, scale = 4)
    private BigDecimal minInvestmentAmount = BigDecimal.ZERO;
    private int minDirectReferrals;
    @Column(precision = 19, scale = 4)
    private BigDecimal minReferralTotalDeposit = BigDecimal.ZERO;
    @Column(precision = 19, scale = 4)
    private BigDecimal minReferralTotalInvestment = BigDecimal.ZERO;
    @Column(precision = 19, scale = 4)
    private BigDecimal minTotalEarnings = BigDecimal.ZERO;

    private int txnPerDay;
    // Required downline users per level (depth = 1 = level A, etc.)
    @ElementCollection //  Tells JPA this is a collection of simple values (not entities).
    @CollectionTable(name = "rank_downline_requirements", joinColumns = @JoinColumn(name = "rank_id")) // Specifies a separate table to store the map.
    @MapKeyColumn(name = "depth") // The key of the map will be stored as depth (e.g., 1 = level A, 2 = level B...).
    @Column(name = "required_count") // The value of the map — how many users are required at that depth.
    private Map<Integer, Integer> requiredLevelCounts = new HashMap<>();

    private BigDecimal commissionPercentage;
    private int rankBonus;
    private String description;

    private boolean active;


    // ###################### Extra Fields ###################
    private boolean achievable; // Can user reach this rank currently?
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType rewardType = RewardType.CASH;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RankType rankType = RankType.PERFORMANCE;


    public enum RewardType {
        CASH,
        PRODUCT,
        POINTS
    }

    public enum RankType {
        PERFORMANCE,
        STATIC,
        PROMOTIONAL
    }
    // #########################################

    public RankConfigV2(String rankCode, String displayName, BigDecimal minDepositAmount, BigDecimal minInvestmentAmount, BigDecimal commissionPercentage) {
        this.code = rankCode;
        this.displayName = displayName;
        this.minDepositAmount = minDepositAmount;
        this.minInvestmentAmount = minInvestmentAmount;
        this.commissionPercentage = commissionPercentage;
    }

    public RankConfigV2(String rankCode, String displayName, int minDepositAmount, int minInvestmentAmount, float commissionPercentage) {
        this.code = rankCode;
        this.displayName = displayName;
        this.minDepositAmount = new BigDecimal(minDepositAmount);
        this.minInvestmentAmount = new BigDecimal(minInvestmentAmount);
        this.commissionPercentage = new BigDecimal(String.valueOf(commissionPercentage));
    }

    public int getMinDirectReferrals() {
        return this.requiredLevelCounts.get(1);
    }

}
