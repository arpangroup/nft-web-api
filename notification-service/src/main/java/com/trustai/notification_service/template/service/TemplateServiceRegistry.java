package com.trustai.notification_service.template.service;

import com.trustai.notification_service.template.enums.TemplateType;
import com.trustai.notification_service.template.entity.Template;
import com.trustai.notification_service.template.service.impl.EmailTemplateService;
import com.trustai.notification_service.template.service.impl.PushNotificationTemplateService;
import com.trustai.notification_service.template.service.impl.SmsTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Helps in resolving services dynamically by type. Useful.
 */
@Component
@RequiredArgsConstructor
public class TemplateServiceRegistry {
    private final Map<TemplateType, TemplateService<? extends Template>> serviceMap; // If more channels are expected in the future:
    private final EmailTemplateService emailService;
    private final SmsTemplateService smsService;
    private final PushNotificationTemplateService pushService;

    public TemplateService<?> getService(TemplateType type) {
        TemplateService<?> service = serviceMap.get(type);
        if (service == null) {
            throw new IllegalArgumentException("No template service found for type: " + type);
        }
        return service;
    }

    public TemplateService<?> getService(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> emailService;
            case "sms" -> smsService;
            case "push" -> pushService;
            default -> throw new IllegalArgumentException("Unsupported template type: " + type);
        };
    }
}
