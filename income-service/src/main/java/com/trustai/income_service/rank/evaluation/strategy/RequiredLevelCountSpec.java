package com.trustai.income_service.rank.evaluation.strategy;

import com.trustai.income_service.rank.entity.RankConfig;
import com.trustai.income_service.rank.evaluation.RankSpecification;
import com.trustai.user_service.hierarchy.dto.UserMetrics;
import com.trustai.user_service.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RequiredLevelCountSpec implements RankSpecification {
    @Override
    public boolean isSatisfied(User user, UserMetrics metrics, RankConfig config) {
        Map<Integer, Integer> required = config.getRequiredLevelCounts();
        Map<Integer, Long> actual = metrics.getUserHierarchyStats().getDepthWiseCounts();

        for (Map.Entry<Integer, Integer> entry : required.entrySet()) {
            if (actual.getOrDefault(entry.getKey(), 0L) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }
}
