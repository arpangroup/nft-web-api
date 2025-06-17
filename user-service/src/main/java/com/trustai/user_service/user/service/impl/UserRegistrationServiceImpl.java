package com.trustai.user_service.user.service.impl;

import com.trustai.common.enums.TriggerType;
import com.trustai.common.event.UserRegisteredEvent;
import com.trustai.user_service.user.dto.RegistrationRequest;
import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.entity.VerificationToken;
import com.trustai.user_service.user.enums.VerificationType;
import com.trustai.user_service.user.mapper.UserMapper;
import com.trustai.user_service.user.repository.VerificationTokenRepository;
import com.trustai.user_service.user.service.RegistrationService;
import com.trustai.user_service.user.validation.RegistrationRequestValidatorTemplate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationServiceImpl implements RegistrationService {
    private final UserProfileServiceImpl userService;
    private final VerificationTokenRepository tokenRepo;
    private final RegistrationRequestValidatorTemplate requestValidatorTemplate;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher publisher;
    private final PasswordEncoder passwordEncoder;

    private static final Duration EXPIRY_DURATION = Duration.ofMinutes(15);

    // Step 1: Request Email Verification
    public void requestEmailVerification(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address");
        }

        String verificationCode = generateVerificationCode();
        LocalDateTime expiryTime = LocalDateTime.now().plus(EXPIRY_DURATION);

        VerificationToken token = tokenRepo.findByTargetAndType(email, VerificationToken.VerificationType.EMAIL)
                .orElse(new VerificationToken());

        token.setTarget(email);
        token.setType(VerificationType.EMAIL);
        token.setVerificationCode(verificationCode);
        token.setExpiresAt(expiryTime);
        token.setVerified(false);
        token.setCreatedAt(LocalDateTime.now());

        tokenRepo.save(token);

        // Send email with code
        String subject = "Your Email Verification Code";
        String message = "Your verification code is: " + verificationCode;
        log.info("trigger email verification notification to email: {}", email);
        //publishEvent(SEND_EMAIL_VERIFICATION);
        //emailService.sendEmail(email, subject, message);
    }

    // Step 2: Verify Email with Code
    public void verifyEmailCode(String email, String code) {
        VerificationToken token = tokenRepo.findByTargetAndType(email, VerificationType.EMAIL)
                .orElseThrow(() -> new IllegalArgumentException("No verification request found for this email"));

        if (token.isVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        if (!token.getVerificationCode().equals(code)) {
            throw new IllegalArgumentException("Invalid verification code");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification code expired");
        }

        token.setVerified(true);
        tokenRepo.save(token);
    }

    // Step 2: Complete Registration
    @Transactional
    public User registerUser(@NonNull RegistrationRequest request) {
        log.info("registerUser: {}", request);

        VerificationToken token = tokenRepo.findByTargetAndType(request.getEmail(), VerificationType.EMAIL)
                .orElseThrow(() -> new IllegalArgumentException("Email not verified"));

        if (!token.isVerified()) {
            throw new IllegalArgumentException("Email verification not completed");
        }

        /*if (userRepo.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }*/

        requestValidatorTemplate.validateRegistrationRequest(request);

        // create the new user
        log.info("creating user.......");
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setPhone(request.getMobile());
        newUser.setReferralCode(request.getReferralCode());
        newUser.setCreatedAt(LocalDateTime.now());


        User user = userService.createUser(newUser, request.getReferralCode());
        tokenRepo.delete(token); // Optionally delete after successful registration

        log.info("publishing UserRegisteredEvent.....");
        publisher.publishEvent(new UserRegisteredEvent(user.getId(), user.getReferrer().getId(), TriggerType.FIRST_DEPOSIT));
        return user;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900_000) + 100_000); // 6-digit code
    }


}
