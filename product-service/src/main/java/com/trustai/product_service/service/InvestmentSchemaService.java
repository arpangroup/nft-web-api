package com.trustai.product_service.service;

import com.trustai.product_service.entity.investment.InvestmentSchema;

import java.util.List;

public interface InvestmentSchemaService {
    List<InvestmentSchema> getAllSchemas();
    InvestmentSchema getSchemaById(Long id);
    InvestmentSchema createSchema(InvestmentSchema investmentSchema);
}
