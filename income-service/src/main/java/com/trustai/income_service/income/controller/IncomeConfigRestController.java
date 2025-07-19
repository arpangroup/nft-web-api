package com.trustai.income_service.income.controller;

import com.trustai.income_service.income.entity.TeamRebateConfig;
import com.trustai.income_service.income.service.TeamRebateConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/income/configs")
@RequiredArgsConstructor
public class IncomeConfigRestController {
    private final TeamRebateConfigService teamRebateConfigService;

    @GetMapping
    public List<TeamRebateConfig> getAllTeamConfigs() {
        return teamRebateConfigService.getAll();
    }

    @PutMapping
    public ResponseEntity<Void> updateTeamConfigs(@RequestBody List<TeamRebateConfig> updatedConfigs) {
        teamRebateConfigService.updateTeamConfigs(updatedConfigs);
        return ResponseEntity.ok().build();
    }
}
