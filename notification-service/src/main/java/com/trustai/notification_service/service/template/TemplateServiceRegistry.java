package com.trustai.notification_service.service.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemplateServiceRegistry {
    private final EmailTemplateService emailService;
    private final SmsTemplateService smsService;
    private final PushNotificationTemplateService pushService;

    public TemplateService<?> getService(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> emailService;
            case "sms" -> smsService;
            case "push" -> pushService;
            default -> throw new IllegalArgumentException("Unsupported template type: " + type);
        };
    }
}
