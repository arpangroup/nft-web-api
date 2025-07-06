package com.trustai.income_service.rank.repository;

import com.trustai.income_service.rank.entity.Rank;
import com.trustai.income_service.rank.entity.RankConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankConfigRepository extends JpaRepository<RankConfig, Rank> {
    Optional<RankConfig> findByRank(Rank rank);
}
