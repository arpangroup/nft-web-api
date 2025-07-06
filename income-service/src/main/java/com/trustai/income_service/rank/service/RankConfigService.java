package com.trustai.income_service.rank.service;

import com.trustai.income_service.rank.dto.RankConfigDto;
import com.trustai.income_service.rank.entity.Rank;
import com.trustai.income_service.rank.entity.RankConfig;
import com.trustai.income_service.rank.repository.RankConfigRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankConfigService {
    private final RankConfigRepository rankConfigRepository;

    // Fetch all RankConfig entities and convert to DTOs
    public List<RankConfigDto> getAllRankConfigs() {
        return rankConfigRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Update RankConfigs from DTOs
    @Transactional
    public void updateRankConfigs(List<RankConfigDto> dtos) {
        for (RankConfigDto dto : dtos) {
            Rank rankEnum = Rank.valueOf(dto.getRank());
            RankConfig existing = rankConfigRepository.findById(rankEnum)
                    .orElse(new RankConfig(rankEnum, 0, 0, new BigDecimal("0"))); // Create if not exists

            existing.setMinWalletBalance(dto.getMinWalletBalance());
            existing.setMaxWalletBalance(dto.getMaxWalletBalance());
            existing.setTxnPerDay(dto.getTxnPerDay());
            existing.setCommissionRate(dto.getCommissionRate());
            existing.setRequiredLevelCounts(new HashMap<>(dto.getRequiredLevelCounts()));

            rankConfigRepository.save(existing);
        }
    }

    // Helper to convert Entity to DTO
    private RankConfigDto convertToDto(RankConfig config) {
        RankConfigDto dto = new RankConfigDto();
        dto.setRank(config.getRank().name());
        dto.setMinWalletBalance(config.getMinWalletBalance());
        dto.setMaxWalletBalance(config.getMaxWalletBalance());
        dto.setTxnPerDay(config.getTxnPerDay());
        dto.setCommissionRate(config.getCommissionRate());
        dto.setRequiredLevelCounts(new HashMap<>(config.getRequiredLevelCounts()));
        dto.setStakeValue(config.getStakeValue());
        return dto;
    }
}
