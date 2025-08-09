package com.trustai.income_service.income.repository;

import com.trustai.income_service.income.entity.TeamIncomeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamIncomeConfigRepository extends JpaRepository<TeamIncomeConfig, String> {
    Optional<TeamIncomeConfig> findByRankCode(String rankCode);
}
