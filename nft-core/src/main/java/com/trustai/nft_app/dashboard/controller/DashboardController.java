package com.trustai.nft_app.dashboard.controller;


import com.trustai.nft_app.dashboard.dto.UserDashboardDTO;
import com.trustai.nft_app.dashboard.service.DashboardAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardAggregationService dashboardService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDashboardDTO> getDashboard(@PathVariable Long userId) {
        UserDashboardDTO dashboard = dashboardService.getUserDashboardData(userId);
        return ResponseEntity.ok(dashboard);
    }
}
