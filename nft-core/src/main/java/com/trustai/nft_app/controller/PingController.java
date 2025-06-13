package com.trustai.nft_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Set;

@RefreshScope
@RestController
public class PingController {
    private static final Instant START_TIME = Instant.now();

    @Autowired
    private ContextRefresher contextRefresher;


    @Value("${bonus.referral.flat-amount}")
    public String bonusAmount;

    @GetMapping("/ping")
    public String ping() {
        return "UP....." + bonusAmount;
    }

    @GetMapping("/refresh") // curl -X POST http://localhost:8080/actuator/refresh
    public String triggerRefresh() {
        Set<String> refreshed = contextRefresher.refresh();
        return "Refreshed properties: " + refreshed;
    }
}
