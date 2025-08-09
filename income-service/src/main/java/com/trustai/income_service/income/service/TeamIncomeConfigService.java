package com.trustai.income_service.income.service;

import com.trustai.income_service.income.entity.TeamIncomeConfig;
import com.trustai.income_service.income.repository.TeamIncomeConfigRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamIncomeConfigService {
    private final TeamIncomeConfigRepository teamIncomeConfigRepository;


    public List<TeamIncomeConfig> getAll() {
        List<TeamIncomeConfig> list = teamIncomeConfigRepository.findAll().stream()
                .collect(Collectors.toList());
        return list;
    }


    @Transactional
    public void updateTeamConfigs(List<TeamIncomeConfig> updatedConfigs) {
        for (TeamIncomeConfig config : updatedConfigs) {
            Optional<TeamIncomeConfig> existingOpt = teamIncomeConfigRepository.findById(config.getRankCode());
            if (existingOpt.isPresent()) {
                TeamIncomeConfig existing = existingOpt.get();
                existing.setIncomePercentages(config.getIncomePercentages());
                teamIncomeConfigRepository.save(existing);
            } else {
                teamIncomeConfigRepository.save(config); // in case it's new
            }
        }
    }
}
