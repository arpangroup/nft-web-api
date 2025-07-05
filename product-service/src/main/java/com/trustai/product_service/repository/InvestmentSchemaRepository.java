package com.trustai.product_service.repository;

import com.trustai.product_service.entity.investment.InvestmentSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentSchemaRepository extends JpaRepository<InvestmentSchema, Long> {
}
