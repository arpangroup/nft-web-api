package com.trustai.notification_service.service;

import com.trustai.notification_service.entity.EmailTemplate;
import com.trustai.notification_service.entity.PushNotificationTemplate;
import com.trustai.notification_service.entity.SmsTemplate;
import org.springframework.data.domain.Page;

public interface TemplateService {
    Page<EmailTemplate> getEmailTemplates(Integer page, Integer size);
    Page<SmsTemplate> getSmsTemplates(Integer page, Integer size);
    Page<PushNotificationTemplate> getPushTemplates(Integer page, Integer size);

    EmailTemplate getEmailTemplateById(Long id);
    SmsTemplate getSmsTemplateById(Long id);
    PushNotificationTemplate getPushTemplateById(Long id);


    EmailTemplate getEmailTemplateByCode(String code);
    SmsTemplate getSmsTemplateByCode(String code);
    PushNotificationTemplate getPushTemplateByCode(String code);
}
