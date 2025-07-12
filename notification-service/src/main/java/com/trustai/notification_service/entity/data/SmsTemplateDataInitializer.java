package com.trustai.notification_service.entity.data;

import com.trustai.notification_service.entity.NotificationCode;
import com.trustai.notification_service.entity.SmsTemplate;
import com.trustai.notification_service.repository.SmsTemplateRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SmsTemplateDataInitializer {
    private final SmsTemplateRepository smsTemplateRepository;
    private final String TEMPLATE_FOR_ADMIN = "Admin";
    private final String TEMPLATE_FOR_USER = "USER";

    @PostConstruct
    public void init() {
        List<SmsTemplate> smsTemplates = getTemplates();
        smsTemplateRepository.saveAll(smsTemplates);
    }

    private List<SmsTemplate> getTemplates() {
        return List.of(
            new SmsTemplate(NotificationCode.USER_MAIL_SEND)
                    .setMessageBody("""
                            Thanks for joining us  {{full_name}}\n
                            {{message}}
                            """),

                new SmsTemplate(NotificationCode.USER_INVESTMENT)
                        .setMessageBody("""
                               Hello!\n
                               {{txn}}. 'Successfully Investment\n
                               {{plan_name}}\n
                               {{invest_amount}}
                            """),
                new SmsTemplate(NotificationCode.USER_ACCOUNT_DISABLED),
                new SmsTemplate(NotificationCode.MANUAL_DEPOSIT_REQUEST)
                        .setMessageBody("""
                                The manual deposit request details:\n
                                {{txn}}\n
                                {{gateway_name}}\n
                                {{deposit_amount}}
                                """),
                new SmsTemplate(NotificationCode.WITHDRAW_REQUEST)
                        .setMessageBody("""
                                Withdraw Request details:\n
                                {{txn}}\n
                                {{method_name}}\n
                                {{withdraw_amount}}
                                """),
                new SmsTemplate(NotificationCode.KYC_REQUEST)
                        .setMessageBody("""
                                Thanks for joining our platform! ---  {{site_title}}\n
                                                    
                                {{full_name}}
                                {{email}}
                                
                                As a member of our platform, you can mange your account, buy or sell cryptocurrency, invest and earn profits.
                                
                                Find out more about in - {{site_url}}
                                """),
                new SmsTemplate(NotificationCode.KYC_ACTION)
                        .setMessageBody("""
                                Thanks for joining our platform! ---  {{site_title}}\n
                                                    
                                {{message}}\n
                                {{full_name}}\n
                                {{email}}\n\n
                                
                                As a member of our platform, you can mange your account, buy or sell cryptocurrency, invest and earn profits.\n\n
                                
                                Find out more about in - {{site_url}}
                                """),
                new SmsTemplate(NotificationCode.INVEST_ROI)
                        .setMessageBody("""
                                Hello!\n
                                {{txn}}. 'Successfully Investment\n
                                {{plan_name}}\n
                                {{invest_amount}}\n
                                {{roi}}
                                """),
                new SmsTemplate(NotificationCode.INVESTMENT_END)
                        .setMessageBody("""
                                Hello!\n
                                {{txn}}. 'Successfully Investment End\n
                                {{plan_name}}\n
                                {{invest_amount}}
                                """),
                new SmsTemplate(NotificationCode.WITHDRAW_REQUEST_ACTION)
                        .setMessageBody("""
                                Withdraw Request details:
                                {{message}}\n
                                {{txn}}\n
                                {{method_name}}
                                {{withdraw_amount}}
                                {{status}}
                                """),
                new SmsTemplate(NotificationCode.MANUAL_DEPOSIT_REQUEST_ACTION)
                        .setMessageBody("""
                                The manual deposit request details:\n
                                [[message]]\n
                                [[txn]]\n
                                [[gateway_name]]\n
                                [[deposit_amount]]\n
                                [[status]]
                                """)
        );
    }

}
