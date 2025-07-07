package com.trustai.user_service.hierarchy.service;

import com.trustai.user_service.hierarchy.UserHierarchy;
import com.trustai.user_service.hierarchy.dto.UserHierarchyStats;
import com.trustai.user_service.hierarchy.dto.UserMetrics;
import com.trustai.user_service.hierarchy.repository.UserHierarchyRepository;
import com.trustai.user_service.transaction.service.DepositService;
import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.exception.IdNotFoundException;
import com.trustai.user_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMetricsService {
    private final UserHierarchyRepository hierarchyRepo;
    private final UserRepository userRepository;
    private final DepositService depositService;

    public UserMetrics computeMetrics(Long userId) {
        List<UserHierarchy> downlines = hierarchyRepo.findByAncestor(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new IdNotFoundException("userId " + userId + " not found"));

        // Aggregate team counts by depth
        Map<Integer, Long> depthCounts = downlines.stream()
                .filter(uh -> uh.getDepth() > 0)
                .collect(Collectors.groupingBy(
                        UserHierarchy::getDepth,
                        Collectors.counting()
                ));

        long teamSize = depthCounts.values().stream().mapToLong(Long::longValue).sum();

        UserHierarchyStats stats = UserHierarchyStats.builder()
                .depthWiseCounts(depthCounts)
                .totalTeamSize(teamSize)
                .build();

        return UserMetrics.builder()
                .directReferrals(depthCounts.getOrDefault(1, 0L).intValue())
                .userHierarchyStats(stats)
                .totalDeposit(depositService.getTotalDeposit(userId))
                //.totalInvestment()
                .walletBalance(user.getWalletBalance())
                //.totalEarnings()
                .build();
    }
}
