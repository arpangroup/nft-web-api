package com.trustai.common.api.impl;

import com.trustai.common.api.UserApi;
import com.trustai.common.dto.UserHierarchyDto;
import com.trustai.common.dto.UserInfo;
import com.trustai.common.dto.UserMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
//@ConditionalOnProperty(name = "user.api.rest.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class UserApiRestClientImpl implements UserApi {
    private final RestClient restClient;

    public UserApiRestClientImpl(
            @Qualifier("userServiceRestClient") RestClient restClient
    ) {
        this.restClient = restClient;
    }

    @Override
    public List<UserInfo> getUsers() {
        log.info("Calling getUsers");
        UserInfo[] users = restClient.get()
                .uri("/users")
                .retrieve()
                //.body(new ParameterizedTypeReference<List<UserInfo>>() {});
                .body(UserInfo[].class);
        return Arrays.asList(users);
    }

    @Override
    public List<UserInfo> getUsers(List<Long> userIds) {
        log.info("Calling getUsers with userIds={}", userIds);
        UserInfo[] response = restClient.post()
                .uri("/users/by-ids")
//                .uri(uriBuilder ->
//                        uriBuilder
//                                .path("/by-ids")
//                                //.queryParam("userIds", queryParam)
//                                .build()
//                )
                .body(userIds)
                .retrieve()
                //.body(new ParameterizedTypeReference<List<UserInfo>>() {});
                .body(UserInfo[].class);

        return Arrays.asList(response);
    }

    @Override
    public UserInfo getUserById(Long userId) {
        log.info("Calling getUserById with userId={}", userId);
        return restClient.get()
                .uri("/users/{userId}", userId)
                .retrieve()
                .body(UserInfo.class);
    }

    @Override
    public void updateRank(Long userId, String rankCode) {
        log.info("Calling updateRank with userId={}, rankCode={}", userId, rankCode);
        restClient.put()
                .uri("/users/{userId}/rank", userId)
                .body(rankCode)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void updateWalletBalance(Long userId, BigDecimal updatedNewBalance) {
        log.info("Calling updateWalletBalance with userId={}, updatedNewBalance={}", userId, updatedNewBalance);
        restClient.put()
                .uri("/users/{userId}/wallet-balance", userId)
                .body(updatedNewBalance)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public UserMetrics computeMetrics(Long userId) {
        log.info("Calling computeMetrics with userId={}", userId);
        return restClient.get()
                .uri("/users/{id}/metrics", userId)
                .retrieve()
                .body(UserMetrics.class);
    }

    @Override
    public List<UserHierarchyDto> findByDescendant(Long descendant) {
        log.info("Calling findByDescendant with descendantId={}", descendant);
        UserHierarchyDto[] response = restClient.get()
                .uri("/hierarchy/descendant/{id}", descendant)
                .retrieve()
                .body(UserHierarchyDto[].class);
        return Arrays.asList(response);
    }

}
