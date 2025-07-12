package com.trustai.notification_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "push_templates")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PushNotificationTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code; // templateName

    private String title;

    private String messageBody;

    private String templateFor;
    private boolean templateActive = true;

    public PushNotificationTemplate(String code) {
        this.code = code;
    }
    public PushNotificationTemplate(NotificationCode code) {
        this(code.name());
    }
}
