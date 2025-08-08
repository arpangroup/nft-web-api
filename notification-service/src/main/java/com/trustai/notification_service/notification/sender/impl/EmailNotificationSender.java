package com.trustai.notification_service.notification.sender.impl;

import com.trustai.notification_service.dto.NotificationRequest;
import com.trustai.notification_service.notification.enums.NotificationChannel;
import com.trustai.notification_service.notification.sender.NotificationSender;
import com.trustai.notification_service.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationSender implements NotificationSender {
    private final EmailService emailService;

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public void send(NotificationRequest request) {
        emailService.sendMail(
                request.getRecipient(),
                request.getSubject(),
                request.getMessage()
        );
    }
}
