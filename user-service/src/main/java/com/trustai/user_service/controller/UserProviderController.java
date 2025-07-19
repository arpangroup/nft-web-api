package com.trustai.user_service.controller;

import com.trustai.common.dto.UserInfo;
import com.trustai.common.dto.UserMetrics;
import com.trustai.user_service.hierarchy.UserHierarchy;
import com.trustai.user_service.hierarchy.repository.UserHierarchyRepository;
import com.trustai.user_service.hierarchy.service.UserMetricsService;
import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.exception.IdNotFoundException;
import com.trustai.user_service.user.mapper.UserMapper;
import com.trustai.user_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/provider/users")
@RequiredArgsConstructor
@Slf4j
public class UserProviderController {
    private final UserRepository userRepository;
    private final UserHierarchyRepository userHierarchyRepository;
    private final UserMetricsService userMetricsService;
    private final UserMapper mapper;

    @GetMapping
    public List<UserInfo> getUsers() {
        log.info("Received request to get all users");
        List<UserInfo> users = userRepository.findAll().stream().map(mapper::mapTo).toList();
        log.info("Returning {} users", users.size());
        return users;
    }

    @GetMapping("/batch")
    public List<UserInfo> getUserByIds(@RequestBody List<Long> ids) {
        log.info("Received request to get users by IDs: {}", ids);
        List<UserInfo> users = userRepository.findByIdIn(ids).stream().map(mapper::mapTo).toList();
        log.info("Returning {} users for requested IDs", users.size());
        return users;
    }

    @GetMapping("/{userId}")
    public UserInfo getUserById(@PathVariable Long userId) {
        log.info("Received request to get user by ID: {}", userId);
        return userRepository.findById(userId)
                .map(user -> {
                    log.info("User found for ID: {}", userId);
                    return mapper.mapTo(user);
                })
                .orElseThrow(() -> {
                    log.error("User not found for ID: {}", userId);
                    return new IdNotFoundException("userId: " + userId + " not found");
                });
    }

    @PutMapping("/{userId}/{rankCode}")
    public void updateRank(@PathVariable Long userId, String rankCode) {
        log.info("Received request to update rank for userId: {} with rankCode: {}", userId, rankCode);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User not found for ID: {}", userId);
            return new IdNotFoundException("userId: " + userId + " not found");
        });
        user.setRankCode(rankCode);
        userRepository.save(user);
        log.info("Updated rankCode to '{}' for userId: {}", rankCode, userId);
    }

    @GetMapping("/hierarchy/{descendant}")
    public List<UserHierarchy> findByDescendant(@PathVariable Long descendant) {
        log.info("Received request to get user hierarchy for descendant: {}", descendant);
        List<UserHierarchy> hierarchy = userHierarchyRepository.findByDescendant(descendant);
        log.info("Returning {} hierarchy records for descendant: {}", hierarchy.size(), descendant);
        return hierarchy;
    }

    @GetMapping("/metrics/{userId}")
    public UserMetrics computeMetrics(@PathVariable Long userId) {
        log.info("findByDescendant for userId: {}", userId);
        return userMetricsService.computeMetrics(userId);
    }

    @PutMapping("/updateWalletBalance/{userId}/{updatedNewBalance}")
    public void updateWalletBalance(@PathVariable Long userId, @PathVariable BigDecimal updatedNewBalance) {
        log.info("Received request to update wallet balance for userId: {} with new balance: {}", userId, updatedNewBalance);

        // First, check if the user exists
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User not found for ID: {}", userId);
            return new IdNotFoundException("userId: " + userId + " not found");
        });

        // Update the wallet balance on the user entity
        user.setWalletBalance(updatedNewBalance);

        // Persist the change
        userRepository.save(user);

        log.info("Successfully updated wallet balance to {} for userId: {}", updatedNewBalance, userId);
    }
}
