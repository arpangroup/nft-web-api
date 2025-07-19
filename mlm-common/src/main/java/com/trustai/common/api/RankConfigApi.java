package com.trustai.common.api;

import com.trustai.common.dto.RankConfigDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/*@FeignClient(
        name = "IncomeService.RankConfigClient",
        url = CommonConstants.BASE_URL,
        path = CommonConstants.PATH_TRANSACTION_SERVICE,
        configuration = FeignConfig.class
)*/
public interface RankConfigApi {//code/{rankCode}
    @GetMapping("/code/{rankCode}")
    RankConfigDto getRankConfigByRankCode(@PathVariable String rankCode);
}
