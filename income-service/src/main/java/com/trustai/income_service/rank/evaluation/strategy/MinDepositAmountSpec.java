package com.trustai.income_service.rank.evaluation.strategy;

import com.trustai.income_service.rank.entity.RankConfig;
import com.trustai.income_service.rank.evaluation.RankSpecification;
import com.trustai.user_service.hierarchy.dto.UserMetrics;
import com.trustai.user_service.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class MinDepositAmountSpec implements RankSpecification {

    @Override
    public boolean isSatisfied(User user, UserMetrics metrics, RankConfig config) {
        return metrics.getTotalDeposit()
                .compareTo(config.getMinDepositAmount()) >= 0;
    }
}
