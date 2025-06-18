package com.trustai.user_service.auth.service;

import com.trustai.user_service.user.dto.*;
import jakarta.servlet.http.HttpServletRequest;

public interface RegistrationService {
    void initiateRegistration(InitiateRegistrationRequest request, HttpServletRequest servletRequest);
    void requestEmailVerification(String email);
    void verifyEmail(VerifyEmailRequest request);
    void completeRegistration(CompleteRegistrationRequest request);
}
