package com.trustai.investment_service.service;

import com.trustai.investment_service.entity.InvestmentSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface SchemaService {
    Page<InvestmentSchema> getAllSchemas(Pageable pageable);
    InvestmentSchema getSchemaById(Long id);
    InvestmentSchema createSchema(InvestmentSchema investmentSchema);
    InvestmentSchema updateSchema(Long id, Map<String, Object> updates);
}
