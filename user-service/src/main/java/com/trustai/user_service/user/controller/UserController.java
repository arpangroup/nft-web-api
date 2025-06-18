package com.trustai.user_service.user.controller;

import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.mapper.UserMapper;
import com.trustai.user_service.user.repository.UserRepository;
import com.trustai.user_service.auth.service.RegistrationService;
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

    /*@PostMapping("/register")
    public ResponseEntity<UserInfo> registerUser(@Valid @RequestBody RegistrationRequest request) throws InvalidRequestException {
        User user = registrationService.registerUser(request);
        return ResponseEntity.ok(mapper.mapTo(user));
    }*/

    @GetMapping("/add-users")
    public ResponseEntity<?> registerUser() {
        //addDummyUsers();
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /*private void addDummyUsers() {
        User root = userRepository.findById(1L).orElseThrow(()-> new IdNotFoundException("root userId not found"));

        User user1 = registerUser(new User("user1"), root.getReferralCode());
        User user2 = registerUser(new User("user2"), root.getReferralCode());
        User user3 = registerUser(new User("user3"), root.getReferralCode());

        User user1_1 = registerUser(new User("user1_1"), user1.getReferralCode());
        User user1_2 = registerUser(new User("user1_2"), user1.getReferralCode());
        User user2_1 = registerUser(new User("user2_1"), user2.getReferralCode());
        User user2_2 = registerUser(new User("user2_2"), user2.getReferralCode());
        User user3_1 = registerUser(new User("user3_1"), user3.getReferralCode());

        User user1_1_1 = registerUser(new User("user1_1_1"), user1_1.getReferralCode());
        User user1_1_2 = registerUser(new User("user1_1_2"), user1_1.getReferralCode());
        User user2_1_1 = registerUser(new User("user2_1_1"), user2_1.getReferralCode());
        User user2_1_2 = registerUser(new User("user2_1_2"), user2_1.getReferralCode());
        User user3_1_1 = registerUser(new User("user3_1_1"), user3_1.getReferralCode());

        User user1_1_1_1 = registerUser(new User("user1_1_1_1"), user1_1_1.getReferralCode());
        User user1_1_2_1 = registerUser(new User("user1_1_2_1"), user1_1_2.getReferralCode());
        User user1_1_2_2 = registerUser(new User("user1_1_2_2"), user1_1_2.getReferralCode());
        User user3_1_1_1 = registerUser(new User("user3_1_1_1"), user3_1_1.getReferralCode());


        User user1_1_2_2_1 = registerUser(new User("user1_1_2_2_1"), user1_1_2_2.getReferralCode());
        User user1_1_2_2_2 = registerUser(new User("user1_1_2_2_2"), user1_1_2_2.getReferralCode());
        User user3_1_1_1_1 = registerUser(new User("user3_1_1_1_1"), user3_1_1_1.getReferralCode());
        User user3_1_1_1_2 = registerUser(new User("user3_1_1_1_2"), user3_1_1_1.getReferralCode());


        User x = registerUser(new User("x"), user2_1_1.getReferralCode());
        User y = registerUser(new User("Y"), user2_1_1.getReferralCode());

        User x1 = registerUser(new User("x1"), x.getReferralCode());
        User x2 = registerUser(new User("x2"), x.getReferralCode());

        User y1 = registerUser(new User("y1"), y.getReferralCode());
        User y2 = registerUser(new User("y2"), y.getReferralCode());


        User x2_1 = registerUser(new User("x2_1"), x2.getReferralCode());
        User x2_2 = registerUser(new User("x2_2"), x2.getReferralCode());
    }*/

    /*private User registerUser(User user, final String referralCode) throws InvalidRequestException {
        //return userService.createUser(user, referralCode);

        RegistrationRequest request = new RegistrationRequest();
        request.setUsername(user.getUsername());
        request.setReferralCode(referralCode);
        return registrationService.registerUser(request);
    }*/
}
