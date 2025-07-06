//package com.trustai.income_service.rank.service.impl;
//
//import com.arpangroup.nft_common.dto.UserInfo;
//import com.arpangroup.referral_service.client.UserClient;
//import com.arpangroup.referral_service.hierarchy.service.UserHierarchyService;
//import com.arpangroup.referral_service.rank.evaluation.RankEvaluationContext;
//import com.arpangroup.referral_service.rank.evaluation.RankEvaluationStrategy;
//import com.arpangroup.referral_service.rank.model.Rank;
//import com.arpangroup.referral_service.rank.service.RankEvaluationService;
//import com.trustai.income_service.rank.service.RankEvaluationService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class RankEvaluationServiceImpl implements RankEvaluationService {
//    private final List<RankEvaluationStrategy> strategies;
//    private final UserHierarchyService hierarchyService;
//    private final UserClient userClient;
//
//    public Rank evaluateUserRank(Long userId, BigDecimal walletBalance) {
//        log.info("evaluateUserRank for userId: {}", userId);
//        Map<Integer, List<Long>> downlineByLevel = hierarchyService.getDownlinesGroupedByLevel(userId);
//        RankEvaluationContext context = new RankEvaluationContext(userId, walletBalance, downlineByLevel);
//
//        return strategies.stream()
//                .filter(strategy -> strategy.isEligible(userId, context))
//                .map(RankEvaluationStrategy::getRank)
//                .max(Comparator.comparing(Enum::ordinal)) // Choose highest eligible rank
//                .orElse(Rank.RANK_1); // Default rank
//    }
//
//    public void evaluateAndUpdateUserRank(Long userId, BigDecimal walletBalance) {
//        log.info("evaluateAndUpdateUserRank for userId: {}", userId);
//        //Rank currentRank = userClient.getUserInfo(userId).getRank();
//        Rank evaluatedRank = evaluateUserRank(userId, walletBalance);
//
//        /*if (!evaluatedRank.equals(currentRank)) {
//            userClient.updateUserRank(userId, evaluatedRank.getValue());
//        }*/
//
//        userClient.updateUserRank(userId, evaluatedRank.getValue());
//    }
//
//    @Override
//    public void evaluateAndUpdateUserWithReferrerRank(Long userId, BigDecimal walletBalance, Long referrerId) {
//        log.info("evaluateAndUpdateUserWithReferrerRank for userId: {}", userId);
//
//        log.info("processing Referee's Rank for userId: {}............", userId);
//        evaluateAndUpdateUserRank(userId, walletBalance);
//
//        log.info("getUserInfo for referrerId: {}............", referrerId);
//        UserInfo referrer = userClient.getUserInfo(referrerId);
//        if (referrer != null) {
//            log.info("processing Referrer rank for userId: {}, referrerId: {}............", userId, referrerId);
//            evaluateAndUpdateUserRank(referrer.getId(), walletBalance);
//        }
//    }
//
//
//
//
//    /*private final LevelHandler levelChain;
//
//    public RankEvaluationService() {
//        Level1Handler level1 = new Level1Handler();
//        Level2Handler level2 = new Level2Handler();
//        Level3Handler level3 = new Level3Handler();
//        //Level4Handler level4 = new Level4Handler();
//
//        level1.setNext(level2);
//        level2.setNext(level3);
//        //level3.setNext(level4);
//
//        this.levelChain = level1;
//    }
//
//    public int determineLevel(User user) {
//        LevelCalculationContext context = new LevelCalculationContext(); // precompute counts here
//        context.setUserId(user.getId());
//        return levelChain.determineLevel(user, context);
//    }*/
//}
