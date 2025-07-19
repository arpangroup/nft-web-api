package com.trustai.common.api.impl;

import com.trustai.common.api.RankConfigApi;
import com.trustai.common.dto.RankConfigDto;
import org.springframework.stereotype.Service;

@Service
public class RankConfigApiRestClientImpl implements RankConfigApi {
    @Override
    public RankConfigDto getRankConfigByRankCode(String rankCode) {
        return null;
    }
}
