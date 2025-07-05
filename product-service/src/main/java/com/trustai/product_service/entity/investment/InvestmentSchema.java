package com.trustai.product_service.entity.investment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    @OneToOne(optional = false)
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

    @Column(updatable = false)
    private java.time.LocalDateTime createdAt;

    private java.time.LocalDateTime updatedAt;

    private String createdBy; // Useful for audit trail

    private String updatedBy;

    private String currency; // e.g., USD, INR – especially if multi-currency support is needed
    private BigDecimal earlyExitPenalty; // Penalty if exited before full duration
    private String termsAndConditionsUrl; // For linking external T&C


    public enum SchemaType {
        FIXED, // Fixed interest or terms
        RANGE // Variable based on amount/period
    }

    public enum InterestCalculationType {
        FLAT,         // Fixed amount (e.g., $50)
        PERCENTAGE,   // Relative amount (e.g., 5% of something)
    }


    public enum ReturnType {
        PERIOD,
        LIFETIME
    }
}
