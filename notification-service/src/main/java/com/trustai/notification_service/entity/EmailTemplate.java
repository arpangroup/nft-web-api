package com.trustai.notification_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "email_templates")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EmailTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // unique template code

    @Column(unique = true, nullable = false)
    private String subject;

    private String banner;
    private String title;
    private String salutation;
    private String messageBody;

    private String buttonLevel;
    private String buttonLink;

    private boolean enableFooterStatus;
    private String footerBody;
    private boolean enableFooterBottom;
    private String bottomTitle;
    private String bottomBody;

    private boolean templateActive;

    public EmailTemplate(String code, String subject) {
        this.code = code;
        this.subject = subject;
    }

    public EmailTemplate(NotificationCode code, String subject) {
        this(code.name(), subject);
    }

}
