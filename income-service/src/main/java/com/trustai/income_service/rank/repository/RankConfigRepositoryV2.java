package com.trustai.income_service.rank.repository;

import com.trustai.income_service.rank.entity.RankConfig;
import com.trustai.income_service.rank.entity.RankConfigV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankConfigRepositoryV2 extends JpaRepository<RankConfigV2, Long> {
    Optional<RankConfig> findByCode(String code);
}
