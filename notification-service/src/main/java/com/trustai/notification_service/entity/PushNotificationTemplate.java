package com.trustai.notification_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "push_templates")
@Data
@NoArgsConstructor
public class PushNotificationTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // unique template code

    private String title;

    private String messageBody;

    private boolean templateActive;
}
