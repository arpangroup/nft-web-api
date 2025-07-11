package com.trustai.notification_service.strategy;


import com.trustai.notification_service.enums.NotificationType;

public interface NotificationStrategy {
    NotificationType getType();
    void send(String recipient, String subject, String content);
}
