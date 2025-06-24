package com.trustai.user_service.controller;

import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.mapper.UserMapper;
import com.trustai.user_service.user.repository.UserRepository;
import com.trustai.user_service.auth.service.RegistrationService;
import com.trustai.user_service.user.service.UserAccountService;
import com.trustai.user_service.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserProfileService userService;
    private final UserAccountService userAccountService;
    private final RegistrationService registrationService;
    private final UserMapper mapper;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> users() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserInfo(@PathVariable Long userId) {
        log.info("getUserInfo for User ID: {}......", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateUserInfo(@PathVariable Long userId, @RequestBody Map<String, Object> fieldsToUpdate) {
        log.info("updateUserInfo for User ID: {}, fieldsToUpdate: {}......", userId, fieldsToUpdate);
        return ResponseEntity.ok(userService.updateUser(userId, fieldsToUpdate));
    }

    @PutMapping("/{userId}/account-status")
    public ResponseEntity<User> updateAccountStatus(@PathVariable Long userId, @RequestParam User.AccountStatus status) {
        User user = userAccountService.updateAccountStatus(userId, status);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/transaction-status")
    public ResponseEntity<User> updateTransactionStatus(@PathVariable Long userId,
                                        @RequestParam(required = false) User.TransactionStatus depositStatus,
                                        @RequestParam(required = false) User.TransactionStatus withdrawStatus,
                                        @RequestParam(required = false) User.TransactionStatus sendMoneyStatus
    ) {
        User user =  userAccountService.updateTransactionStatus(userId, depositStatus, withdrawStatus, sendMoneyStatus);
        return ResponseEntity.ok(user);
    }

}
