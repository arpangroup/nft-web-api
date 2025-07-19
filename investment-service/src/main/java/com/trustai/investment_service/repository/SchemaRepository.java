package com.trustai.investment_service.repository;

import com.trustai.investment_service.entity.InvestmentSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchemaRepository extends JpaRepository<InvestmentSchema, Long> {
    List<InvestmentSchema> findByLinkedRank(String linkedRank);
    Page<InvestmentSchema> findByLinkedRank(String linkedRank, Pageable pageable);
}
