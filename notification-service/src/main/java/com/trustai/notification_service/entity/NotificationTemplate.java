package com.trustai.notification_service.entity;

import com.trustai.notification_service.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_template")
@Data
@NoArgsConstructor
public class NotificationTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // unique template code

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType = NotificationType.EMAIL;

    @Column(nullable = false)
    private String subject;

    @Lob
    private String content; // use {{var}} placeholders

    public NotificationTemplate(String code, NotificationType notificationType, String subject, String content) {
        this.code = code;
        this.notificationType = notificationType;
        this.subject = subject;
        this.content = content;
    }

    public static NotificationTemplate email(NotificationCode code, String subject, String content) {
        return new NotificationTemplate(code.name(), NotificationType.EMAIL, subject, content);
    }

    public static NotificationTemplate sms(NotificationCode code, String content) {
        return new NotificationTemplate(code.name(), NotificationType.SMS, "", content);
    }

    public static NotificationTemplate push(NotificationCode code, String subject, String content) {
        return new NotificationTemplate(code.name(), NotificationType.PUSH, subject, content);
    }
}
