package com.trustai.notification_service.controller;

import com.trustai.common.dto.ApiResponse;
import com.trustai.notification_service.dto.NotificationRequest;
import com.trustai.notification_service.entity.NotificationCode;
import com.trustai.notification_service.enums.NotificationType;
import com.trustai.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/sample")
    public ResponseEntity<String> send(@RequestBody NotificationRequest request) {
        log.info("Received notification request: {}", request);
        try {
            notificationService.sendNotification(request);
            log.info("Notification sent successfully to {}", request.getRecipient());
            return ResponseEntity.ok("Notification sent");
        } catch (Exception ex) {
            log.error("Failed to send notification to {}: {}", request.getRecipient(), ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Notification failed");
        }
    }

    @PostMapping("/mail-connection-test")
    public ResponseEntity<ApiResponse> mailConnectionTest(@RequestBody NotificationRequest request) {
        log.info("Received notification request: {}", request);
        try {
            request.setType(NotificationType.EMAIL);
            request.setTemplateCode(NotificationCode.MAIL_CONNECTION_TEST.name());
            notificationService.sendNotification(request);
            log.info("Notification sent successfully to {}", request.getRecipient());
            return ResponseEntity.ok(ApiResponse.success("Notification sent"));
        } catch (Exception ex) {
            log.error("Failed to send notification to {}: {}", request.getRecipient(), ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Notification failed"));
        }
    }
}
