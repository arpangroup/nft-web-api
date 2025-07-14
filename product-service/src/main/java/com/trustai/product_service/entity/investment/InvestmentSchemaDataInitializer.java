package com.trustai.product_service.entity.investment;

import com.trustai.common.enums.CurrencyType;
import com.trustai.product_service.repository.InvestmentSchemaRepository;
import com.trustai.product_service.repository.ScheduleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InvestmentSchemaDataInitializer {
    private final InvestmentSchemaRepository investmentSchemaRepository;
    private final ScheduleRepository scheduleRepository;

    @PostConstruct
    public void init() {
        // Create Schedules
        Schedule schedule1 = new Schedule(null, "2 Week", 336); // 14 days
        Schedule schedule2 = new Schedule(null, "Hourly", 1);
        Schedule schedule3 = new Schedule(null, "Daily", 24);
        Schedule schedule4 = new Schedule(null, "Weekly", 168);
        Schedule schedule5 = new Schedule(null, "Monthly", 720);

        schedule1 = scheduleRepository.save(schedule1);
        schedule2 = scheduleRepository.save(schedule2);
        schedule3 = scheduleRepository.save(schedule3);
        schedule4 = scheduleRepository.save(schedule4);
        schedule5 = scheduleRepository.save(schedule5);

        // Investment Schema 1 - FIXED + PERIOD + cancellable
        InvestmentSchema schema1 = new InvestmentSchema();
        schema1.setTitle("Fixed 1-Year Plan");
        schema1.setSchemaBadge("FIXED_PLAN");
        schema1.setSchemaType(InvestmentSchema.SchemaType.FIXED);
        schema1.setMinimumInvestmentAmount(new BigDecimal("1000.00"));
        schema1.setReturnRate(new BigDecimal("6.5"));
        schema1.setInterestCalculationMethod(InvestmentSchema.InterestCalculationType.PERCENTAGE);
        schema1.setReturnSchedule(schedule4);
        schema1.setReturnType(InvestmentSchema.ReturnType.PERIOD);
        schema1.setTotalReturnPeriods(52);
        schema1.setCapitalReturned(true);
        schema1.setFeatured(true);
        schema1.setCancellable(true);
        schema1.setCancellationGracePeriodMinutes(1440);
        schema1.setTradeable(false);
        schema1.setActive(true);
        schema1.setDescription("Fixed 1-Year investment with weekly returns.");
        schema1.setCreatedAt(LocalDateTime.now());
        schema1.setUpdatedAt(LocalDateTime.now());
        schema1.setCreatedBy("admin");
        schema1.setUpdatedBy("admin");
        schema1.setCurrency(CurrencyType.USD);
        schema1.setEarlyExitPenalty(new BigDecimal("50.00"));
        schema1.setTermsAndConditionsUrl("https://example.com/tc/fixed1yr");
        investmentSchemaRepository.save(schema1);

        // Investment Schema 2 - RANGE + LIFETIME + not cancellable
        InvestmentSchema schema2 = new InvestmentSchema();
        schema2.setTitle("Flexible Lifetime Growth Plan");
        schema2.setSchemaBadge("LIFETIME_PLAN");
        schema2.setSchemaType(InvestmentSchema.SchemaType.RANGE);
        schema2.setMinimumInvestmentAmount(new BigDecimal("500.00"));
        schema2.setMaximumInvestmentAmount(new BigDecimal("10000.00"));
        schema2.setReturnRate(new BigDecimal("4.0"));
        schema2.setInterestCalculationMethod(InvestmentSchema.InterestCalculationType.FLAT);
        schema2.setReturnSchedule(schedule3);
        schema2.setReturnType(InvestmentSchema.ReturnType.LIFETIME);
        schema2.setCapitalReturned(false);
        schema2.setFeatured(false);
        schema2.setCancellable(false);
        schema2.setTradeable(true);
        schema2.setActive(true);
        schema2.setDescription("Lifetime income with flexible investment range.");
        schema2.setCreatedAt(LocalDateTime.now());
        schema2.setUpdatedAt(LocalDateTime.now());
        schema2.setCreatedBy("system");
        schema2.setUpdatedBy("system");
        schema2.setCurrency(CurrencyType.INR);
        schema2.setEarlyExitPenalty(new BigDecimal("100.00"));
        schema2.setTermsAndConditionsUrl("https://example.com/tc/flexiblelife");
        investmentSchemaRepository.save(schema2);

        // Investment Schema 3 - FIXED + LIFETIME + cancellable
        InvestmentSchema schema3 = new InvestmentSchema();
        schema3.setTitle("Fixed Income for Life");
        schema3.setSchemaBadge("CRYPTO");
        schema3.setSchemaType(InvestmentSchema.SchemaType.FIXED);
        schema3.setMinimumInvestmentAmount(new BigDecimal("2500.00"));
        schema3.setReturnRate(new BigDecimal("5.25"));
        schema3.setInterestCalculationMethod(InvestmentSchema.InterestCalculationType.PERCENTAGE);
        schema3.setReturnSchedule(schedule5);
        schema3.setReturnType(InvestmentSchema.ReturnType.LIFETIME);
        schema3.setCapitalReturned(false);
        schema3.setFeatured(true);
        schema3.setCancellable(true);
        schema3.setCancellationGracePeriodMinutes(4320);
        schema3.setTradeable(true);
        schema3.setActive(false);
        schema3.setDescription("Monthly lifetime returns on a fixed deposit.");
        schema3.setCreatedAt(LocalDateTime.now());
        schema3.setUpdatedAt(LocalDateTime.now());
        schema3.setCreatedBy("manager");
        schema3.setUpdatedBy("manager");
        schema3.setCurrency(CurrencyType.EUR);
        schema3.setEarlyExitPenalty(new BigDecimal("75.00"));
        schema3.setTermsAndConditionsUrl("https://example.com/tc/lifetimefixed");
        investmentSchemaRepository.save(schema3);

        // Investment Schema 4 - RANGE + PERIOD + not cancellable
        InvestmentSchema schema4 = new InvestmentSchema();
        schema4.setTitle("Dynamic Tiered Plan");
        schema4.setSchemaBadge("DYNAMIC");
        schema4.setSchemaType(InvestmentSchema.SchemaType.RANGE);
        schema4.setMinimumInvestmentAmount(new BigDecimal("1000.00"));
        schema4.setMaximumInvestmentAmount(new BigDecimal("20000.00"));
        schema4.setReturnRate(new BigDecimal("7.0"));
        schema4.setInterestCalculationMethod(InvestmentSchema.InterestCalculationType.PERCENTAGE);
        schema4.setReturnSchedule(schedule1);
        schema4.setReturnType(InvestmentSchema.ReturnType.PERIOD);
        schema4.setTotalReturnPeriods(26);
        schema4.setCapitalReturned(true);
        schema4.setFeatured(false);
        schema4.setCancellable(false);
        schema4.setTradeable(true);
        schema4.setActive(true);
        schema4.setDescription("Tiered returns for a range of investments.");
        schema4.setCreatedAt(LocalDateTime.now());
        schema4.setUpdatedAt(LocalDateTime.now());
        schema4.setCreatedBy("admin");
        schema4.setUpdatedBy("admin");
        schema4.setCurrency(CurrencyType.USD);
        schema4.setEarlyExitPenalty(new BigDecimal("150.00"));
        schema4.setTermsAndConditionsUrl("https://example.com/tc/dynamictier");
        investmentSchemaRepository.save(schema4);
    }
}
