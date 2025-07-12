package com.trustai.notification_service.entity.data;

import com.trustai.notification_service.entity.NotificationCode;
import com.trustai.notification_service.entity.PushNotificationTemplate;
import com.trustai.notification_service.repository.PushNotificationTemplateRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PushNotificationTemplateDataInitializer {
    private final PushNotificationTemplateRepository pushTemplateRepository;
    private final String TEMPLATE_FOR_ADMIN = "Admin";
    private final String TEMPLATE_FOR_USER = "USER";

    @PostConstruct
    public void init() {
        List<PushNotificationTemplate> smsTemplates = getTemplates();
        pushTemplateRepository.saveAll(smsTemplates);
    }

    private List<PushNotificationTemplate> getTemplates() {
        return List.of(
            new PushNotificationTemplate(NotificationCode.NEW_USER)
                    .setTitle("Wellcome to {{full_name}}")
                    .setMessageBody("""
                            Thanks for joining us  {{full_name}}\n
                                                        
                            {{message}}
                            """)
                    .setTemplateFor(TEMPLATE_FOR_ADMIN)
                    .setTemplateActive(true),

            new PushNotificationTemplate(NotificationCode.MANUAL_DEPOSIT_REQUEST)
                    .setTitle("Manual Deposit request")
                    .setMessageBody("""
                           The manual deposit request details:\n
                           {{txn}}\n
                           {{gateway_name}}\n
                           {{deposit_amount}}
                            """)
                    .setTemplateFor(TEMPLATE_FOR_ADMIN)
                    .setTemplateActive(true),
            new PushNotificationTemplate(NotificationCode.WITHDRAW_REQUEST)
                    .setTitle("Withdraw Request")
                    .setMessageBody("""
                           Withdraw Request details:\n
                           {{txn}}\n
                           {{method_name}}\n
                           {{withdraw_amount}}
                            """)
                    .setTemplateFor(TEMPLATE_FOR_ADMIN)
                    .setTemplateActive(true),
            new PushNotificationTemplate(NotificationCode.KYC_REQUEST)
                    .setTitle("Kyc Request")
                    .setMessageBody("""
                            {{full_name}} Kyc requested\n
                            {{email}}
                            """)
                    .setTemplateFor(TEMPLATE_FOR_ADMIN)
                    .setTemplateActive(true),
            new PushNotificationTemplate(NotificationCode.KYC_ACTION),
            new PushNotificationTemplate(NotificationCode.USER_INVESTMENT_START)
                    .setTitle("You Invested on {{plan_name}}")
                    .setMessageBody("""
                           Hello!\n
                           {{txn}} 'Successfully Investment\n
                           {{plan_name}}\n
                           {{invest_amount}}
                            """)
                    .setTemplateFor(TEMPLATE_FOR_ADMIN)
                    .setTemplateActive(true),
            new PushNotificationTemplate(NotificationCode.INVESTED_ON_PROFIT)
                    .setTitle("You Invested on {{plan_name}}")
                    .setMessageBody("""
                           Hello!\n
                           {{txn}} 'Successfully Investment\n
                           {{plan_name}}\n
                           {{invest_amount}}
                           {{roi}}
                            """)
                    .setTemplateFor(TEMPLATE_FOR_ADMIN)
                    .setTemplateActive(true),
            new PushNotificationTemplate(NotificationCode.INVESTMENT_END)
                    .setTitle("You Invested on {{plan_name}}")
                    .setMessageBody("""
                           Hello!\n
                           {{txn}} 'Successfully Investment End\n
                           {{plan_name}}\n
                           {{invest_amount}}
                            """)
                    .setTemplateFor(TEMPLATE_FOR_ADMIN)
                    .setTemplateActive(true),
            new PushNotificationTemplate(NotificationCode.WITHDRAW_REQUEST_ACTION)
                    .setTitle("Withdraw Request")
                    .setMessageBody("""
                           Withdraw Request details:\n
                           {{message}}
                           {{txn}}\n
                           {{method_name}}\n
                           {{withdraw_amount}}\n
                           {{status}}
                            """),
            new PushNotificationTemplate(NotificationCode.MANUAL_DEPOSIT_REQUEST_ACTION)
                    .setTitle("Manual Deposit request")
                    .setMessageBody("""
                           The manual deposit request details:\n
                           {{message}}
                           {{txn}}\n
                           {{gateway_name}}\n
                           {{deposit_amount}}\n
                           {{status}}
                            """)
        );
    }

}
