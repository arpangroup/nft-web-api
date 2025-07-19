package com.trustai.mlm_rank_service.service;

import com.trustai.common.api.UserApi;
import com.trustai.common.dto.UserInfo;
import com.trustai.mlm_rank_service.entity.RankConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankCalculationOrchestrationService {
    private final RankEvaluatorService rankEvaluatorService;
    private final UserApi userApi; // REST or Feign client to user-service
    //private final UserActivityLogService activityLogService;

    public void reevaluateRank(Long userId, String triggerSource) {
        log.info("Evaluating rank for userId={} due to {}", userId, triggerSource);
        UserInfo userInfo = userApi.getUserById(userId);

        Optional<RankConfig> newRankOpt = rankEvaluatorService.evaluate(userInfo);

        newRankOpt.ifPresent(newRank -> {
            String currentRankCode = userInfo.getRankCode();

            if (!newRank.getCode().equals(currentRankCode)) {
                log.info("Updating rank for userId={} from {} → {}", userId, currentRankCode, newRank.getCode());
                userApi.updateRank(userId, newRank.getCode());

                //activityLogService.save(UserActivityLog.rankChanged(userId, currentRankCode, newRank.getCode(), triggerSource));
            } else {
                log.debug("UserId={} already holds the correct rank {}", userId, currentRankCode);
            }
        });
    }
}
