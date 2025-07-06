package com.trustai.income_service.rank.controller;

import com.trustai.income_service.rank.dto.RankConfigDto;
import com.trustai.income_service.rank.repository.RankConfigRepository;
import com.trustai.income_service.rank.service.RankConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rankings")
@RequiredArgsConstructor
@Slf4j
public class RankConfigController {
    private final RankConfigService rankConfigService;

    @GetMapping
    public List<RankConfigDto> getRankConfig() {
        // Convert entity to DTO including requiredLevelCounts map
        return rankConfigService.getAllRankConfigs();
    }

    @PostMapping
    public ResponseEntity<?> updateRank(@RequestBody List<RankConfigDto> updatedConfigs) {
        rankConfigService.updateRankConfigs(updatedConfigs);
        return ResponseEntity.ok().build();
    }

}
