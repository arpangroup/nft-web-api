package com.trustai.product_service.service;

import com.trustai.product_service.entity.investment.InvestmentSchema;
import com.trustai.product_service.entity.investment.Schedule;
import com.trustai.product_service.exception.ResourceNotFoundException;
import com.trustai.product_service.repository.InvestmentSchemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvestmentSchemaServiceImpl implements InvestmentSchemaService {
    private final InvestmentSchemaRepository schemaRepository;

    @Override
    public Page<InvestmentSchema> getAllSchemas(Pageable pageable) {
        log.info("Fetching all investment schemas...");
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return schemaRepository.findAll(pageable);
    }

    @Override
    public InvestmentSchema getSchemaById(Long id) {
        log.info("Fetching schema with ID: {}", id);
        return schemaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Investment schema not found for ID: " + id));
    }

    @Override
    public InvestmentSchema createSchema(InvestmentSchema investmentSchema) {
        investmentSchema.setCreatedAt(java.time.LocalDateTime.now());
        investmentSchema.setUpdatedAt(java.time.LocalDateTime.now());
        log.info("Creating new investment schema: {}", investmentSchema.getTitle());
        return schemaRepository.save(investmentSchema);
    }

    @Override
    public InvestmentSchema updateSchema(Long id, Map<String, Object> updates) {
        InvestmentSchema schema = getSchemaById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "title" -> schema.setTitle((String) value);
                case "schemaBadge" -> schema.setSchemaBadge((String) value);
                case "schemaType" -> schema.setSchemaType(InvestmentSchema.SchemaType.valueOf((String) value));
                case "minimumInvestmentAmount" -> schema.setMinimumInvestmentAmount(new BigDecimal(value.toString()));
                case "maximumInvestmentAmount" -> schema.setMaximumInvestmentAmount(new BigDecimal(value.toString()));
                case "returnRate" -> schema.setReturnRate(new BigDecimal(value.toString()));
                case "interestCalculationMethod" -> schema.setInterestCalculationMethod(InvestmentSchema.InterestCalculationType.valueOf((String) value));
                case "returnType" -> schema.setReturnType(InvestmentSchema.ReturnType.valueOf((String) value));
                case "totalReturnPeriods" -> schema.setTotalReturnPeriods((Integer) value);
                case "isCapitalReturned" -> schema.setCapitalReturned((Boolean) value);
                case "isFeatured" -> schema.setFeatured((Boolean) value);
                case "isCancellable" -> schema.setCancellable((Boolean) value);
                case "cancellationGracePeriodMinutes" -> schema.setCancellationGracePeriodMinutes((Integer) value);
                case "isTradeable" -> schema.setTradeable((Boolean) value);
                case "isActive" -> schema.setActive((Boolean) value);
                case "description" -> schema.setDescription((String) value);
                case "currency" -> schema.setCurrency((String) value);
                case "earlyExitPenalty" -> schema.setEarlyExitPenalty(new BigDecimal(value.toString()));
                case "termsAndConditionsUrl" -> schema.setTermsAndConditionsUrl((String) value);
                //case "updatedBy" -> schema.setUpdatedBy((String) value);

                // Special handling for nested Schedule
                /*case "returnScheduleId" -> {
                    Long scheduleId = Long.valueOf(value.toString());
                    Schedule schedule = new Schedule();
                    schedule.setId(scheduleId); // or fetch full entity if needed
                    schema.setReturnSchedule(schedule);
                }*/

                default -> log.warn("Unknown field: {}", key);
            }
        });

        schema.setUpdatedAt(LocalDateTime.now());
        log.info("Partially updating investment schema ID: {}", id);
        return schemaRepository.save(schema);
    }
}
