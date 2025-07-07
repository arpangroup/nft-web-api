package com.trustai.income_service.rank.evaluation.strategy;

import com.trustai.income_service.rank.evaluation.RankSpecification;
import com.trustai.user_service.hierarchy.dto.UserMetrics;
import com.trustai.income_service.rank.entity.RankConfig;
import com.trustai.user_service.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class MinDirectReferralsSpec implements RankSpecification {

    @Override
    public boolean isSatisfied(User user, UserMetrics metrics, RankConfig config) {
        int directReferrals = metrics.getDirectReferrals(); // from precomputed stats
        return directReferrals >= config.getMinDirectReferrals();
    }
}
