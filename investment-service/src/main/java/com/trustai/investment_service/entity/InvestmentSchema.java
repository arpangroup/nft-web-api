package com.trustai.investment_service.entity;

import com.trustai.common.enums.CurrencyType;
import com.trustai.investment_service.enums.InterestCalculationType;
import com.trustai.investment_service.enums.PayoutMode;
import com.trustai.investment_service.enums.ReturnType;
import com.trustai.investment_service.enums.SchemaType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "investment_schemas")
@Data
@NoArgsConstructor
public class InvestmentSchema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private String schemaBadge;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SchemaType schemaType;

    @Column(name = "min_invest_amt", precision = 19, scale = 4)
    private BigDecimal minimumInvestmentAmount;
    @Column(name = "max_invest_amt", precision = 19, scale = 4)
    private BigDecimal maximumInvestmentAmount;

    @Column(precision = 19, scale = 4)
    private BigDecimal returnRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterestCalculationType interestCalculationMethod;

//    @OneToOne(optional = false)
//    @JoinColumn(name = "schedule_id", nullable = false)
    @ManyToOne(optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule returnSchedule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReturnType returnType;

    private int totalReturnPeriods; // totalReturnPeriods

    private boolean isCapitalReturned;
    private boolean isFeatured;

    private boolean isCancellable;
    private int cancellationGracePeriodMinutes;

    private boolean isTradeable;
    private boolean isActive;

    private String description; // Schema summary for UI/API display

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyType currency = CurrencyType.USDT; // e.g., USD, INR – especially if multi-currency support is needed
    private BigDecimal earlyExitPenalty; // Penalty if exited before full duration
    private String termsAndConditionsUrl; // For linking external T&C

    // ########################### PAYOUT #####################################
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayoutMode payoutMode = PayoutMode.DAILY;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "investment_schema_payout_days", joinColumns = @JoinColumn(name = "schema_id"))
    @Column(name = "day_of_week")
    private Set<DayOfWeek> payoutDays; // For WEEKLY mode ==> Enum: MONDAY, TUESDAY, ...

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "investment_schema_payout_dates", joinColumns = @JoinColumn(name = "schema_id"))
    @Column(name = "day_of_month")
    private Set<Integer> payoutDates; //  For MONTHLY mode ==> Valid values: 1 to 31
    // ########################### ./PAYOUT #####################################


    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(nullable = false, updatable = true) private LocalDateTime updatedAt;
    @Column private String createdBy;
    @Column private String updatedBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
