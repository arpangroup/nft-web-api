package com.trustai.user_service.user.service.impl;

import com.trustai.common.enums.TriggerType;
import com.trustai.common.event.UserRegisteredEvent;
import com.trustai.user_service.user.dto.RegistrationRequest;
import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.mapper.UserMapper;
import com.trustai.user_service.user.service.UserRegistrationService;
import com.trustai.user_service.user.validation.RegistrationRequestValidatorTemplate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationServiceImpl implements UserRegistrationService {
    private final UserProfileServiceImpl userService;
    private final RegistrationRequestValidatorTemplate requestValidatorTemplate;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public User registerUser(@NonNull RegistrationRequest request) {
        log.info("registerUser: {}", request);
        requestValidatorTemplate.validateRegistrationRequest(request);

        // create the new user
        log.info("creating user.......");
        User user = userService.createUser(userMapper.mapTo(request), request.getReferralCode());

        log.info("publishing UserRegisteredEvent.....");
        publisher.publishEvent(new UserRegisteredEvent(user.getId(), user.getReferrer().getId(), TriggerType.FIRST_DEPOSIT));
        return user;
    }
}
