package com.trustai.product_service.service;

import com.trustai.product_service.entity.investment.InvestmentSchema;
import com.trustai.product_service.exception.ResourceNotFoundException;
import com.trustai.product_service.repository.InvestmentSchemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvestmentSchemaServiceImpl implements InvestmentSchemaService {
    private final InvestmentSchemaRepository schemaRepository;

    @Override
    public List<InvestmentSchema> getAllSchemas() {
        log.info("Fetching all investment schemas...");
        return schemaRepository.findAll();
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
}
