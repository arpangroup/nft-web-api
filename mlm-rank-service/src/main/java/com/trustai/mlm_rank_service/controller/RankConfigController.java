package com.trustai.mlm_rank_service.controller;

import com.trustai.mlm_rank_service.dto.RankConfigDto;
import com.trustai.mlm_rank_service.entity.RankConfig;
import com.trustai.mlm_rank_service.service.RankConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rankings")
@RequiredArgsConstructor
@Slf4j
public class RankConfigController {
    private final RankConfigService rankConfigService;

    @GetMapping
    public  ResponseEntity<Page<RankConfig>> getRankConfig(Pageable pageable) {
        // Convert entity to DTO including requiredLevelCounts map
        return ResponseEntity.ok(rankConfigService.getAllRankConfigs(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RankConfig> getRankConfigById(@PathVariable Long id) {
        return ResponseEntity.ok(rankConfigService.getRankById(id));
    }

    @GetMapping("/code/{rankCode}")
    public ResponseEntity<RankConfig> getRankConfigByCode(@PathVariable String rankCode) {
        return ResponseEntity.ok(rankConfigService.getRankByRankCode(rankCode));
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

    @PatchMapping("/update")
    public ResponseEntity<?> patchMultipleRanks(@RequestBody List<Map<String, Object>> updatesList) {
        for (Map<String, Object> updateMap : updatesList) {
            Object idObj = updateMap.remove("id");
            if (idObj == null) {
                throw new IllegalArgumentException("Each update must include an 'id' field.");
            }
            Long id = Long.valueOf(idObj.toString());
            rankConfigService.patchRank(id, updateMap);
        }
        return ResponseEntity.ok().build();
    }
}
