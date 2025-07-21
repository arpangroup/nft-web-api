package com.trustai.common.api.impl;

import com.trustai.common.api.RankConfigApi;
import com.trustai.common.dto.RankConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Primary
@Slf4j
public class RankConfigApiRestClientImpl implements RankConfigApi {
    private final RestClient restClient;

    public RankConfigApiRestClientImpl(
            @Qualifier("v1ApiRestClient") RestClient restClient
    ) {
        this.restClient = restClient;
    }

    @Override
    public RankConfigDto getRankConfigByRankCode(String rankCode) {
        log.info("Calling getRankConfigByRankCode with rankCode={}", rankCode);
        return restClient.get()
                .uri("/code/{rankCode}", rankCode)
                .retrieve()
                .body(RankConfigDto.class);
    }
}
