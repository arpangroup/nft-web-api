package com.trustai.income_service.rank.evaluation;

import com.trustai.user_service.hierarchy.dto.UserMetrics;
import com.trustai.income_service.rank.entity.RankConfig;
import com.trustai.user_service.user.entity.User;

public interface RankSpecification {
    boolean isSatisfied(User user, UserMetrics metrics, RankConfig config);
}
