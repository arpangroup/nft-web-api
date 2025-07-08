package com.trustai.mlm_rank_service.service;

import com.trustai.common.client.UserClient;
import com.trustai.common.dto.UserInfo;
import com.trustai.mlm_rank_service.entity.RankConfig;
import com.trustai.user_service.user.entity.UserActivityLog;
import com.trustai.user_service.user.service.UserActivityLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankCalculationOrchestrationService {
    private final RankEvaluatorService rankEvaluatorService;
    private final UserClient userClient; // REST or Feign client to user-service
    private final UserActivityLogService activityLogService;

    public void reevaluateRank(Long userId, String triggerSource) {
        log.info("Evaluating rank for userId={} due to {}", userId, triggerSource);
        UserInfo userInfo = userClient.getUserById(userId);

        Optional<RankConfig> newRankOpt = rankEvaluatorService.evaluate(userInfo);

        newRankOpt.ifPresent(newRank -> {
            String currentRankCode = userClient.getRankCode(userId);

            if (!newRank.getCode().equals(currentRankCode)) {
                log.info("Updating rank for userId={} from {} → {}", userId, currentRankCode, newRank.getCode());
                userClient.updateRank(userId, newRank.getCode());

                activityLogService.save(UserActivityLog.rankChanged(userId, currentRankCode, newRank.getCode(), triggerSource));
            } else {
                log.debug("UserId={} already holds the correct rank {}", userId, currentRankCode);
            }
        });
    }
}
