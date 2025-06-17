package com.trustai.user_service.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private String phone;

    // Balance Related
    @Column(name = "wallet_balance", precision = 19, scale = 4)
    private BigDecimal walletBalance = BigDecimal.ZERO;
    @Column(name = "profit_balance", precision = 19, scale = 4)
    private BigDecimal profitBalance = BigDecimal.ZERO;

    // Referral & User Hierarchy Related:
    @Column(name = "referral_code", unique = true, length = 255)
    private String referralCode;
    @ManyToOne
    @JoinColumn(name = "referrer_id", referencedColumnName = "id")
    private User referrer;
    @Column(name = "rank_level")
    private int rank = 1;

    @OneToOne(optional = false, cascade = CascadeType.ALL) // Makes the association required (not null)
    @JoinColumn(name = "kyc_info", nullable = false) // Maps to the actual foreign key column
    private Kyc kycInfo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public AccountStatus accountStatus = AccountStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Status depositStatus = Status.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Status withdrawStatus = Status.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Status sendMoneyStatus = Status.ACTIVE;


    /*
    private String mobile;

    private int referBy;

    private int posId;
    private int position;
    private int planId;
    private float totalInvest;
    private float totalBinaryCom;
    private float totalRefCom;
    private int dailyAdLimit;

    private Address addressDetails;
    private String image;

    private int status;

    private String KycStatus;
    private int profileComplete;
    private String banReason;

    private String rememberToken;
    private String provider;
    private int providerId;*/


    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PostPersist
    private void setReferralAfterInsert() {
        this.referralCode = "REF" + this.id;
    }

    public User(String username) {
        this.username = username;
        this.setAccountStatus(AccountStatus.PENDING);
    }

    public User(String username, int rank, BigDecimal walletBalance) {
        this(username);
        this.rank = rank;
        this.walletBalance = walletBalance;
        this.setAccountStatus(AccountStatus.PENDING);
    }

    public User(Long id, String username, int rank, BigDecimal walletBalance) {
        this(username, rank, walletBalance);
        this.id = id;
    }

    /*
                    --> DISABLED
                   |
           PENDING  --> ACTIVE  ---> SUSPENDED / BANNED / LOCKED
                   |
                    --> DISABLED


     */
    public enum AccountStatus {
        ACTIVE,         // User is active and allowed to use the system
        DISABLED,       // User is deactivated (manually or due to violation)
        PENDING,        // User registered but hasn't completed verification
        SUSPENDED,      // Temporarily banned for a specific reason/time
        DELETED,        // Soft-deleted user (can be restored later)
        BANNED,         // Permanently banned
        LOCKED,         // Account locked due to security reasons (e.g., too many login attempts)
        INACTIVE        // User hasn’t used the service in a long time
    }

    public enum Status {
        ACTIVE,
        DISABLED,
    }
}
