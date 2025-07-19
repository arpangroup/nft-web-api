package com.trustai.income_service.income.repository;

import com.trustai.income_service.income.entity.TeamRebateConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRebateConfigRepository extends JpaRepository<TeamRebateConfig, String> {
    Optional<TeamRebateConfig> findByRankCode(String rankCode);
}
