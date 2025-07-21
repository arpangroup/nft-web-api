package com.trustai.common.api;

import com.trustai.common.dto.RankConfigDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface RankConfigApi {//code/{rankCode}
    @GetMapping("/code/{rankCode}")
    RankConfigDto getRankConfigByRankCode(@PathVariable String rankCode);
}
