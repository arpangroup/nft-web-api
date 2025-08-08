package com.trustai.notification_service.notification.entity;

import com.trustai.notification_service.notification.enums.NotificationChannel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationChannel type; // EMAIL, SMS, PUSH, IN_APP

    private String title;
    private String message;
    private boolean global; // if true, sent to all users

    private LocalDateTime createdAt;
}
