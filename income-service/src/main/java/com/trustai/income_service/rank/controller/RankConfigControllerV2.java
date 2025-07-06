package com.trustai.income_service.rank.controller;

import com.trustai.income_service.rank.dto.RankConfigDto;
import com.trustai.income_service.rank.dto.RankConfigDtoV2;
import com.trustai.income_service.rank.entity.RankConfigV2;
import com.trustai.income_service.rank.service.RankConfigService;
import com.trustai.income_service.rank.service.RankConfigServiceV2;
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
public class RankConfigControllerV2 {
    private final RankConfigServiceV2 rankConfigService;

    @GetMapping
    public  ResponseEntity<List<RankConfigV2>> getRankConfig() {
        // Convert entity to DTO including requiredLevelCounts map
        return ResponseEntity.ok(rankConfigService.getAllRankConfigs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RankConfigV2> getRankConfigById(@PathVariable Long id) {
        return ResponseEntity.ok(rankConfigService.getRankById(id));
    }

    @PostMapping
    public ResponseEntity<?> createRank(@RequestBody RankConfigDtoV2 request) {
        rankConfigService.createRank(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchRank(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        rankConfigService.patchRank(id, updates);
        return ResponseEntity.ok().build();
    }
}
