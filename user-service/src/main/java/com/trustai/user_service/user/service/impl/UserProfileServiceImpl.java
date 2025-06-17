package com.trustai.user_service.user.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trustai.user_service.user.entity.Kyc;
import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.exception.IdNotFoundException;
import com.trustai.user_service.user.repository.UserRepository;
import com.trustai.user_service.user.service.UserProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/*
When a new user registers, bonuses can be propagated upwards through the referral hierarchy. Here's a simple approach:
    Direct Bonus: The referrer receives a direct bonus when a new user registers using their referral code
    Community Team Rebate: Calculate rebates based on the users level and propagate them upwards.
 */
@Service("userServiceImpl")
@Primary
@RequiredArgsConstructor
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    private final UserRepository userRepository;
//    private final TransactionService transactionService;
//    private final DepositService depositService;

    @Override
    public User createUser(User user, String referralCode) {
        log.info("creating user for username: {}", user.getUsername());
        Kyc kyc = new Kyc();
        user = userRepository.save(user);
        log.info("user created successfully with userId: {}", user.getId());

        // Update the Closure table for user relationship
        User referrer = userRepository.findByReferralCode(referralCode).orElse(null);
        if (referrer != null) {
            log.info("Referrer with ID: {} ====> updating the closure table....", referrer.getId());
            user.setReferrer(referrer);
        }
        return user;
    }

    @Override
    public User updateUser(Long userId, Map<String, Object> fieldsToUpdate) {
        User user = getUserById(userId);

        fieldsToUpdate.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(User.class, key);
            if (field != null) {
                field.setAccessible(true);
                Object convertedValue = new ObjectMapper().convertValue(value, field.getType());
                ReflectionUtils.setField(field, user, convertedValue );
            } else {
                log.warn("Attempted to patch unknown field: {}", key);
            }
        });
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        userRepository.findById(user.getId()).ifPresent(u -> userRepository.save(user));
        return user;
    }

    @Override
    public User updateUserRank(Long userId, int newRank) {
        User user = userRepository.findById(userId).orElseThrow(()-> new IdNotFoundException("userId: " + userId + " not found"));
        user.setRank(newRank);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

//    @Override
//    public List<User> getUserByIds(List<Long> userIds) {
//        return userRepository.findByIdIn(userIds);
//    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new IdNotFoundException("userId: " + userId + " not found"));
    }

//    @Override
//    public User getUserByReferralCode(String referralCode) {
//        return userRepository.findByReferralCode(referralCode).orElseThrow(() -> new IdNotFoundException("invalid referralCode"));
//    }

    @Override
    @Transactional
    public User deposit(long userId, BigDecimal amount, String remarks, String metaInfo) {
//        return depositService.deposit(userId, amount, remarks, metaInfo);
        return null;
    }

    @Override
    public boolean hasDeposit(Long userId) {
//        return depositService.hasDeposit(userId);
        return false;
    }

    @Override
    public BigDecimal getTotalDeposit(Long userId) {
        return null;
    }
}
