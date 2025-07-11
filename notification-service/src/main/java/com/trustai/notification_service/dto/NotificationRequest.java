package com.trustai.notification_service.dto;

import com.trustai.notification_service.enums.NotificationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Data
@NoArgsConstructor
@ToString
public class NotificationRequest {
    private NotificationType type;
    private String recipient;
    private String templateCode;
    private Map<String, String> properties;
}
