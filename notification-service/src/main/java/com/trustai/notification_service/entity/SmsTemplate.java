package com.trustai.notification_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "sms_templates")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class SmsTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code; // templateName

    private String messageBody;

    private String templateFor;
    private boolean templateActive = true;

    public SmsTemplate(String code) {
        this.code = code;
    }

    public SmsTemplate(NotificationCode code) {
        this(code.name());
        this.templateFor = "User";
    }

    public SmsTemplate(NotificationCode code, boolean isForAdmin) {
        this(code.name());
        this.templateFor = isForAdmin ? "Admin" : "User";
    }
}
