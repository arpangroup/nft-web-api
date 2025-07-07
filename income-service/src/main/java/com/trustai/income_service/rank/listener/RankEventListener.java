package com.trustai.income_service.rank.listener;

import com.trustai.common.event.DepositActivityEvent;
import com.trustai.common.event.ReferralJoinedActivityEvent;
import com.trustai.common.event.UserActivatedActivityEvent;
import com.trustai.income_service.rank.service.RankEvaluatorService;
import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankEventListener {
    private final RankEvaluatorService rankEvaluator;
    private final UserProfileService userService;

    @EventListener
    public void onDeposit(DepositActivityEvent event) {
        User user = userService.getUserById(event.getUserId());
        evaluateRank(user);
    }

    @EventListener
    public void handleUserActivated(UserActivatedActivityEvent event) {
        User user = userService.getUserById(event.getUserId());
        evaluateRank(user);
    }

    @EventListener
    public void handleReferralJoined(ReferralJoinedActivityEvent event) {
        User referrer = userService.getUserById(event.getReferrerId());
        evaluateRank(referrer);
    }

    private void evaluateRank(User user) {
        rankEvaluator.evaluate(user).ifPresent(rank -> {
            user.setRankCode(rank.getCode());
            userService.updateUserRank(user.getId(), rank.getCode());
            //userService.save(user);
        });
    }
}
