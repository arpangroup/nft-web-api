package com.trustai.income_service.rank.controller;

import com.trustai.income_service.rank.dto.RankConfigDto;
import com.trustai.income_service.rank.entity.RankConfig;
import com.trustai.income_service.rank.service.RankConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/rankings")
@RequiredArgsConstructor
@Slf4j
public class RankConfigController {
    private final RankConfigService rankConfigService;

    @GetMapping
    public  ResponseEntity<List<RankConfig>> getRankConfig() {
        // Convert entity to DTO including requiredLevelCounts map
        return ResponseEntity.ok(rankConfigService.getAllRankConfigs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RankConfig> getRankConfigById(@PathVariable Long id) {
        return ResponseEntity.ok(rankConfigService.getRankById(id));
    }

    @PostMapping
    public ResponseEntity<?> createRank(@RequestBody RankConfigDto request) {
        rankConfigService.createRank(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchRank(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        rankConfigService.patchRank(id, updates);
        return ResponseEntity.ok().build();
    }
}
