package com.trustai.notification_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNotification {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Notification notification;
    private Long userId;
    private boolean viewed;
    private LocalDateTime viewedAt;
    private LocalDateTime createdAt;

}
