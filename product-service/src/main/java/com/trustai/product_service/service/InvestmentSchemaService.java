package com.trustai.product_service.service;

import com.trustai.product_service.entity.investment.InvestmentSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface InvestmentSchemaService {
    Page<InvestmentSchema> getAllSchemas(Pageable pageable);
    InvestmentSchema getSchemaById(Long id);
    InvestmentSchema createSchema(InvestmentSchema investmentSchema);
    InvestmentSchema updateSchema(Long id, Map<String, Object> updates);
}
