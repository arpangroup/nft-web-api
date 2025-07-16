package com.trustai.investment_service.service;

import com.trustai.common.enums.CurrencyType;
import com.trustai.investment_service.entity.InvestmentSchema;
import com.trustai.investment_service.entity.Schedule;
import com.trustai.investment_service.enums.InterestCalculationType;
import com.trustai.investment_service.enums.ReturnType;
import com.trustai.investment_service.enums.SchemaType;
import com.trustai.investment_service.exception.ResourceNotFoundException;
import com.trustai.investment_service.repository.ScheduleRepository;
import com.trustai.investment_service.repository.SchemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchemaServiceImpl implements SchemaService {
    private final SchemaRepository schemaRepository;
    private final ScheduleRepository scheduleRepository;

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
        log.info("Starting partial update for InvestmentSchema ID: {}", id);

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "title" -> {
                        schema.setTitle((String) value);
                        log.debug("Updated field 'title' to '{}'", value);
                    }
                    case "schemaBadge" -> {
                        schema.setSchemaBadge((String) value);
                        log.debug("Updated field 'schemaBadge' to '{}'", value);
                    }
                    case "schemaType" -> {
                        schema.setSchemaType(SchemaType.valueOf((String) value));
                        log.debug("Updated field 'schemaType' to '{}'", value);
                    }
                    case "minimumInvestmentAmount" -> {
                        schema.setMinimumInvestmentAmount(new BigDecimal(value.toString()));
                        log.debug("Updated field 'minimumInvestmentAmount' to '{}'", value);
                    }
                    case "maximumInvestmentAmount" -> {
                        schema.setMaximumInvestmentAmount(new BigDecimal(value.toString()));
                        log.debug("Updated field 'maximumInvestmentAmount' to '{}'", value);
                    }
                    case "returnRate" -> {
                        schema.setReturnRate(new BigDecimal(value.toString()));
                        log.debug("Updated field 'returnRate' to '{}'", value);
                    }
                    case "interestCalculationMethod" -> {
                        schema.setInterestCalculationMethod(InterestCalculationType.valueOf((String) value));
                        log.debug("Updated field 'interestCalculationMethod' to '{}'", value);
                    }
                    case "returnType" -> {
                        schema.setReturnType(ReturnType.valueOf((String) value));
                        log.debug("Updated field 'returnType' to '{}'", value);
                    }
                    case "totalReturnPeriods" -> {
                        schema.setTotalReturnPeriods(Integer.parseInt((String) value));
                        log.debug("Updated field 'totalReturnPeriods' to '{}'", value);
                    }
                    case "isCapitalReturned", "capitalReturned" -> {
                        schema.setCapitalReturned((Boolean) value);
                        log.debug("Updated field 'isCapitalReturned' to '{}'", value);
                    }
                    case "isFeatured", "featured" -> {
                        schema.setFeatured((Boolean) value);
                        log.debug("Updated field 'isFeatured' to '{}'", value);
                    }
                    case "isCancellable", "cancellable" -> {
                        schema.setCancellable((Boolean) value);
                        log.debug("Updated field 'isCancellable' to '{}'", value);
                    }
                    case "cancellationGracePeriodMinutes" -> {
                        schema.setCancellationGracePeriodMinutes(Integer.parseInt((String) value));
                        log.debug("Updated field 'cancellationGracePeriodMinutes' to '{}'", value);
                    }
                    case "isTradeable", "tradeable" -> {
                        schema.setTradeable((Boolean) value);
                        log.debug("Updated field 'isTradeable' to '{}'", value);
                    }
                    case "isActive", "active" -> {
                        schema.setActive((Boolean) value);
                        log.debug("Updated field 'isActive' to '{}'", value);
                    }
                    case "description" -> {
                        schema.setDescription((String) value);
                        log.debug("Updated field 'description' to '{}'", value);
                    }
                    case "currency" -> {
                        log.debug("Updating field 'currency' to '{}'", value);
                        try {
                            CurrencyType currencyEnum = CurrencyType.valueOf(((String) value).toUpperCase());
                            schema.setCurrency(currencyEnum);
                            log.debug("Updated field 'currency' to '{}'", currencyEnum);
                        } catch (Exception  e) {
                            log.error("Invalid currency value: {}", value);
                            e.printStackTrace();
                        }
                    }
                    case "earlyExitPenalty" -> {
                        schema.setEarlyExitPenalty(new BigDecimal(value.toString()));
                        log.debug("Updated field 'earlyExitPenalty' to '{}'", value);
                    }
                    case "termsAndConditionsUrl" -> {
                        schema.setTermsAndConditionsUrl((String) value);
                        log.debug("Updated field 'termsAndConditionsUrl' to '{}'", value);
                    }
                    case "returnSchedule" -> {
                        log.debug("Updating field 'returnSchedule' to '{}'", value);
                        try {
                            Long scheduleId = Long.parseLong((String) value);
                            Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new ResourceNotFoundException("scheduleId = " + scheduleId + " not found"));
                            schema.setReturnSchedule(schedule);
                            log.debug("Updated field 'returnSchedule' to scheduleId: '{}', scheduleName: '{}' ", scheduleId, schedule.getScheduleName());
                        } catch (Exception e) {
                            log.error("Invalid returnSchedule value: {}", value);
                            e.printStackTrace();
                        }
                    }
                    //case "updatedBy" -> schema.setUpdatedBy((String) value);

                    // Special handling for nested Schedule
                /*case "returnScheduleId" -> {
                    Long scheduleId = Long.valueOf(value.toString());
                    Schedule schedule = new Schedule();
                    schedule.setId(scheduleId); // or fetch full entity if needed
                    schema.setReturnSchedule(schedule);
                }*/

                    default -> log.warn("Unknown field received in update: {}", key);
                }
            } catch (Exception e) {
                log.error("Error updating field '{}': {}", key, e.getMessage(), e);
                throw new IllegalArgumentException("Invalid value for field: " + key);
            }
        });

        schema.setUpdatedAt(LocalDateTime.now());
        log.info("Partially updating investment schema ID: {}", id);
        return schemaRepository.save(schema);
    }
}
