package com.trustai.investment_service.repository;

import com.trustai.investment_service.entity.InvestmentSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemaRepository extends JpaRepository<InvestmentSchema, Long> {
}
