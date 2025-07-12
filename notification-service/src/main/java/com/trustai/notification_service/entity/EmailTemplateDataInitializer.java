package com.trustai.notification_service.entity;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailTemplateDataInitializer {

    @PostConstruct
    public void init() {

    }

    private List<EmailTemplate> getTemplates() {
        return List.of(
                new EmailTemplate(NotificationCode.USER_MAIL_SEND, "{{subject}} for {{full_name}}")
                        .setTitle("Sample Email")
                        .setBanner(null)
                        .setSalutation("Hi {{full_name}},")
                        .setMessageBody("""
                            <p>Thanks for joining us&nbsp;{{site_title}}</p>
                            <p>{{message}}</p>
                            <p>Find out more about in - {{site_url}}</p>
                        """)
                        .setButtonLevel("Login Your Account")
                        .setButtonLink("https://hyiorio.tdevs.co/login")
                        .setEnableFooterStatus(true)
                        .setFooterBody("""
                            <p>Regards</p>
                            <p>{{site_title}}</p>
                        """)
                        .setEnableFooterBottom(false)
                        .setBottomTitle("What is {{site_title}}")
                        .setBottomBody("""
                            Hyiprio is a visual asset manager made for collaboration.
                            Build a central library for your team's visual assets.
                            Empower creation and ensure consistency from your desktop.
                        
                            <p>{{site_url}}</p>
                        """)
                        .setTemplateActive(true),

                new EmailTemplate(NotificationCode.SUBSCRIBER_MAIL_SEND, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.EMAIL_VERIFICATION, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.FORGOT_PASSWORD, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.USER_INVESTMENT, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.USER_ACCOUNT_DISABLED, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.MANUAL_DEPOSIT_REQUEST, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.WITHDRAW_REQUEST, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.ADMIN_FORGET_PASSWORD, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.CONTACT_MAIL_SEND, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.KYC_ACTION, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.INVEST_ROI, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.INVESTMENT_END, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.WITHDRAW_REQUEST_ACTION, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.MANUAL_DEPOSIT_REQUEST_ACTION, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.USER_SUPPORT_TICKET, "{{subject}} for {{full_name}}"),
                new EmailTemplate(NotificationCode.ADMIN_SUPPORT_TICKET, "{{subject}} for {{full_name}}")
        );
    }

}
