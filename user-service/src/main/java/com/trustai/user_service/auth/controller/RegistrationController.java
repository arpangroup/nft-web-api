package com.trustai.user_service.auth.controller;

import com.trustai.user_service.user.dto.CompleteRegistrationRequest;
import com.trustai.user_service.user.dto.InitiateRegistrationRequest;
import com.trustai.user_service.user.dto.VerifyEmailRequest;
import com.trustai.user_service.auth.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/register")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {
    private final RegistrationService registrationService;
    private final HttpServletRequest servletRequest;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiate(@RequestBody InitiateRegistrationRequest request) {
        registrationService.initiateRegistration(request, servletRequest);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailRequest request) {
        registrationService.verifyEmail(request);
        return ResponseEntity.ok("Email verified");
    }

    @PostMapping("/complete")
    public ResponseEntity<?> complete(@RequestBody CompleteRegistrationRequest request) {
        registrationService.completeRegistration(request);
        return ResponseEntity.ok("Registration complete");
    }
}
