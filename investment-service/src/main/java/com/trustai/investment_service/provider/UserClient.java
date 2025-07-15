package com.trustai.investment_service.provider;


import com.trustai.common.dto.UserDetailsInfo;
import com.trustai.common.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "http://localhost:8080",
        path = "/api/v1/users",
        configuration = FeignConfig.class
)
public interface UserClient {
    @GetMapping("/{userId}")
    UserDetailsInfo getUserInfo(@PathVariable Long userId);
}
