package com.trustai.user_service.user.service;

import com.trustai.user_service.user.dto.RegistrationRequest;
import com.trustai.user_service.user.entity.User;
import org.springframework.lang.NonNull;

public interface UserRegistrationService {
    User registerUser(@NonNull RegistrationRequest request);
}
