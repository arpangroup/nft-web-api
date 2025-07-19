package com.trustai.common.api.impl;

import com.trustai.common.api.UserApi;
import com.trustai.common.dto.UserHierarchyDto;
import com.trustai.common.dto.UserInfo;
import com.trustai.common.dto.UserMetrics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
//@ConditionalOnProperty(name = "user.api.rest.enabled", havingValue = "true", matchIfMissing = true)
public class UserApiRestClientImpl implements UserApi {
    private final RestClient restClient;
    private final String baseUrl;

    public UserApiRestClientImpl(
            RestClient.Builder builder,
            @Value("${user.service.url:http://localhost:8080}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
        this.baseUrl = baseUrl;
    }

    @Override
    public List<UserInfo> getUsers() {
        UserInfo[] users = restClient.get()
                .uri("/users")
                .retrieve()
                .body(UserInfo[].class);
        return Arrays.asList(users);
    }

    @Override
    public List<UserInfo> getUsers(List<Long> userIds) {
        UserInfo[] response = restClient.post()
                .uri("/users/by-ids")
                .body(userIds)
                .retrieve()
                .body(UserInfo[].class);

        return Arrays.asList(response);
    }

    @Override
    public UserInfo getUserById(Long userId) {
        return restClient.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .body(UserInfo.class);
    }

    @Override
    public void updateRank(Long userId, String rankCode) {
        restClient.put()
                .uri("/users/{id}/rank", userId)
                .body(rankCode)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void updateWalletBalance(Long userId, BigDecimal updatedNewBalance) {
        restClient.put()
                .uri("/users/{id}/wallet-balance", userId)
                .body(updatedNewBalance)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public List<UserHierarchyDto> findByDescendant(Long descendant) {
        UserHierarchyDto[] response = restClient.get()
                .uri("/hierarchy/descendant/{id}", descendant)
                .retrieve()
                .body(UserHierarchyDto[].class);
        return Arrays.asList(response);
    }

    @Override
    public UserMetrics computeMetrics(Long userId) {
        return restClient.get()
                .uri("/users/{id}/metrics", userId)
                .retrieve()
                .body(UserMetrics.class);
    }
}
