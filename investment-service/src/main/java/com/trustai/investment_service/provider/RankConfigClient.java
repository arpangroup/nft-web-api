package com.trustai.investment_service.provider;

import com.trustai.common.config.FeignConfig;
import com.trustai.common.dto.RankConfigDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "mlm-rank-service",
        url = "http://localhost:8080",
        path = "/api/v1/rankings",
        configuration = FeignConfig.class
)
public interface RankConfigClient {//code/{rankCode}
    @GetMapping("/code/{rankCode}")
    RankConfigDto getRankConfigByRankCode(@PathVariable String rankCode);
}
