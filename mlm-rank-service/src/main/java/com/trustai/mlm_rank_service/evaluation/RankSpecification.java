package com.trustai.mlm_rank_service.evaluation;


import com.trustai.common.dto.UserInfo;
import com.trustai.mlm_rank_service.dto.SpecificationResult;
import com.trustai.mlm_rank_service.entity.RankConfig;
import com.trustai.user_service.hierarchy.dto.UserMetrics;
import com.trustai.user_service.user.entity.User;

public interface RankSpecification {
    boolean isSatisfied(UserInfo user, UserMetrics metrics, RankConfig config);

    default SpecificationResult evaluate(UserInfo user, UserMetrics metrics, RankConfig config) {
        boolean result = isSatisfied(user, metrics, config);
        return new SpecificationResult(result, getClass().getSimpleName(), result ? "Satisfied" : "Not satisfied");
    }
}
