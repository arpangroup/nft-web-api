package com.trustai.user_service.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trustai.user_service.location.IpApiResponse;
import com.trustai.user_service.location.IpLocationService;
import com.trustai.user_service.user.dto.CompleteRegistrationRequest;
import com.trustai.user_service.user.dto.InitiateRegistrationRequest;
import com.trustai.user_service.user.dto.VerifyEmailRequest;
import com.trustai.user_service.user.entity.Kyc;
import com.trustai.user_service.user.entity.RegistrationProgress;
import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.entity.VerificationToken;
import com.trustai.user_service.user.enums.VerificationType;
import com.trustai.user_service.user.exception.AuthenticationException;
import com.trustai.user_service.user.repository.RegistrationProgressRepository;
import com.trustai.user_service.user.repository.UserRepository;
import com.trustai.user_service.user.repository.VerificationTokenRepository;
import com.trustai.user_service.user.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationProgressRepository progressRepo;
    private final VerificationTokenRepository tokenRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileService userService;
    private final IpLocationService ipLocationService;
    private final ApplicationEventPublisher publisher;

    private final Parser userAgentParser = new Parser();
    @Autowired
    private ObjectMapper objectMapper;

    private static final Duration EXPIRY_DURATION = Duration.ofMinutes(15);
    private static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";

    // Step 1: Check username & email availability
    @Override
    public void initiateRegistration(InitiateRegistrationRequest request, HttpServletRequest servletRequest) {
        log.info("initiateRegistration: {}", request);
        if (userRepo.existsByUsername(request.username) || userRepo.existsByEmail(request.email)) {
            throw new AuthenticationException("Username or Email already in use");
        }

        RegistrationProgress progress = progressRepo.findByEmail(request.email)
                .orElse(new RegistrationProgress());

        log.info("log registration progress record....");
        progress.setEmail(request.email);
        progress.setUsername(request.username);
        progress.setMobile(request.mobile);


        progress.setIpAddress(servletRequest.getRemoteAddr());
        progress.setUserAgent(servletRequest.getHeader("User-Agent"));
        progress.setReferrer(servletRequest.getHeader("Referer"));
        // Optional: lookup location by IP and populate below
        progress.setCountry("IN"); // example default or via lookup
        progress.setCity("Kolkata"); // example default or via lookup

        progress.setCreatedAt(LocalDateTime.now());
        progress.setLastUpdated(LocalDateTime.now());
        progress = progressRepo.save(progress);
        log.info("record logged successfully");
        storeIpDetails(progress, servletRequest);

        // Send OTP logic here (save to VerificationToken table)
        requestEmailVerification(request.getEmail());
    }

    @SneakyThrows
    @Async
    private void storeIpDetails(RegistrationProgress progress, HttpServletRequest servletRequest) {
        try {
            //IpApiResponse ipInfo = ipLocationService.fetchIpDetails(servletRequest.getRemoteAddr());
            IpApiResponse ipInfo = null;

            if (ipInfo != null && "success".equals(ipInfo.getStatus())) {
                progress.setCountry(ipInfo.getCountry());
                progress.setCity(ipInfo.getCity());
                /*progress.setRegion(ipInfo.getRegionName());
                progress.setZip(ipInfo.getZip());
                progress.setIsp(ipInfo.getIsp());
                progress.setTimezone(ipInfo.getTimezone());
                progress.setLatitude(ipInfo.getLat());
                progress.setLongitude(ipInfo.getLon());*/
                String json = objectMapper.writeValueAsString(ipInfo);
                progress.setIpDetailsJson(json);

                progressRepo.save(progress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String userAgentHeader = servletRequest.getHeader("User-Agent");
        progress.setUserAgent(userAgentHeader);
        progress.setReferrer(servletRequest.getHeader("Referer"));
        try {
            Client client = userAgentParser.parse(userAgentHeader);
            progress.setDeviceType(client.device.family);
            progress.setDeviceOs(client.os.family);
            progress.setDeviceBrowser(client.userAgent.family + " " + client.userAgent.major);
        } catch (Exception ignored) {}
    }

    // Step 2: Request for verificationCode
    @Override
    public void requestEmailVerification(String email) {
        log.info("requestEmailVerification for email: {}", email);
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address");
        }

        log.info("generating verificationCode for email: {}", email);
        String verificationCode = generateVerificationCode();
        log.info("email: {}, verificationCode: {}", email, verificationCode);
        LocalDateTime expiryTime = LocalDateTime.now().plus(EXPIRY_DURATION);

        VerificationToken token = tokenRepo.findByTypeAndTarget(VerificationType.EMAIL, email)
                .orElse(new VerificationToken());

        token.setTarget(email);
        token.setType(VerificationType.EMAIL);
        token.setCode(verificationCode);
        token.setExpiresAt(expiryTime);
        token.setVerified(false);
        token.setCreatedAt(LocalDateTime.now());

        tokenRepo.save(token);

        // Send email with code
        String subject = "Your Email Verification Code";
        String message = "Your verification code is: " + verificationCode;
        log.info("trigger email verification notification to email: {}, verificationCode: {}", email, verificationCode);
        //publishEvent(SEND_EMAIL_VERIFICATION);
        //emailService.sendEmail(email, subject, message);
    }

    // Step 3: Verify Email with Code
    @Override
    public void verifyEmail(VerifyEmailRequest request) {
        log.info("verifyEmail for email: {}, otp: {}", request.getEmail(), request.getOtp());
        RegistrationProgress progress = progressRepo.findByEmail(request.email)
                .orElseThrow(() -> new AuthenticationException("No registration in progress"));

        VerificationToken token = tokenRepo.findByTypeAndTarget(VerificationType.EMAIL, request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        if (token.isVerified()) {
            throw new IllegalArgumentException("Email already verified");
        }

        if (!token.getCode().equals(request.otp)) {
            throw new IllegalArgumentException("Invalid verification code");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification code expired");
        }

        log.info("token verified for email: {}", request.getEmail());
        token.setVerified(true);
        tokenRepo.save(token);

        progress.setEmailVerified(true);
        progress.setLastUpdated(LocalDateTime.now());
        log.info("update registration progress as verified for email: {}", request.getEmail());
        progressRepo.save(progress);
    }

    // Step 4: Finish the registration process
    @Override
    public void completeRegistration(CompleteRegistrationRequest request) {
        log.info("completeRegistration: {}", request);
        if (!request.getPassword().equals(request.confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        RegistrationProgress progress = progressRepo.findByEmail(request.email)
                .orElseThrow(() -> new AuthenticationException("No registration in progress"));

        if (!progress.isEmailVerified()) {
            throw new AuthenticationException("Email not verified");
        }

        log.info("creating user.......");
        User newUser = new User();
        newUser.setEmail(progress.getEmail());
        newUser.setUsername(progress.getUsername());
        newUser.setMobile(progress.getMobile());
        newUser.setReferralCode(request.referralCode);
        newUser.setPassword(passwordEncoder.encode(request.password));
        newUser.setEmailVerified(true);
        newUser.setCreatedAt(LocalDateTime.now());

        // KYC Info
        Kyc kyc = new Kyc();
        kyc.setEmail(newUser.getEmail());
        kyc.setPhone(newUser.getMobile());
        kyc.setEmailVerifyStatus(progress.isEmailVerified() ? Kyc.EpaStatus.VERIFIED : Kyc.EpaStatus.UNVERIFIED);
        kyc.setPhoneVerifyStatus(progress.isMobileVerified() ? Kyc.EpaStatus.VERIFIED : Kyc.EpaStatus.UNVERIFIED);
        newUser.setKycInfo(kyc);

        userService.createUser(newUser, request.getReferralCode());
        userRepo.save(newUser);

        progress.setRegistrationCompleted(true);
        progressRepo.save(progress);
        //tokenRepo.delete(token); // Optionally delete after successful registration

        log.info("publishing UserRegisteredEvent.....");
        //publisher.publishEvent(new UserRegisteredEvent(newUser.getId(), newUser.getReferrer().getId(), TriggerType.FIRST_DEPOSIT));
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900_000) + 100_000); // 6-digit code
    }

    public IpApiResponse getIpInfo(RegistrationProgress progress) {
        try {
            return objectMapper.readValue(progress.getIpDetailsJson(), IpApiResponse.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader(HEADER_X_FORWARDED_FOR);
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
