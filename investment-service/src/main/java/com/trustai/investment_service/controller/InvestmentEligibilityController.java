package com.trustai.investment_service.controller;

import com.trustai.investment_service.dto.EligibleInvestmentSummary;
import com.trustai.investment_service.service.InvestmentEligibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/investments/eligible-summary")
@RequiredArgsConstructor
public class InvestmentEligibilityController {
    private final InvestmentEligibilityService mappingService;

    @GetMapping
    public ResponseEntity<List<EligibleInvestmentSummary>> getEligibleInvestmentSummaries(@RequestParam("userId") Long userId) {
        List<EligibleInvestmentSummary> result = mappingService.getEligibleInvestmentSummaries(userId);
        return ResponseEntity.ok(result);
    }
}
