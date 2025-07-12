package com.trustai.notification_service.service;

import com.trustai.notification_service.entity.EmailTemplate;
import com.trustai.notification_service.entity.PushNotificationTemplate;
import com.trustai.notification_service.entity.SmsTemplate;
import com.trustai.notification_service.exception.TemplateNotFoundException;
import com.trustai.notification_service.repository.EmailTemplateRepository;
import com.trustai.notification_service.repository.PushNotificationTemplateRepository;
import com.trustai.notification_service.repository.SmsTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateServiceImpl implements TemplateService {
    private final EmailTemplateRepository emailTemplateRepository;
    private final SmsTemplateRepository smsTemplateRepository;
    private final PushNotificationTemplateRepository pushTemplateRepository;

    @Override
    public Page<EmailTemplate> getEmailTemplates(Integer page, Integer size) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return emailTemplateRepository.findAll(pageable);
    }

    @Override
    public Page<SmsTemplate> getSmsTemplates(Integer page, Integer size) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return smsTemplateRepository.findAll(pageable);
    }

    @Override
    public Page<PushNotificationTemplate> getPushTemplates(Integer page, Integer size) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return pushTemplateRepository.findAll(pageable);
    }

    @Override
    public EmailTemplate getEmailTemplateById(Long id) {
        return emailTemplateRepository.findById(id).orElseThrow(() -> new TemplateNotFoundException(id));
    }

    @Override
    public SmsTemplate getSmsTemplateById(Long id) {
        return smsTemplateRepository.findById(id).orElseThrow(() -> new TemplateNotFoundException(id));
    }

    @Override
    public PushNotificationTemplate getPushTemplateById(Long id) {
        return pushTemplateRepository.findById(id).orElseThrow(() -> new TemplateNotFoundException(id));
    }

    @Override
    public EmailTemplate getEmailTemplateByCode(String code) {
        return emailTemplateRepository.findByCode(code).orElseThrow(() -> new TemplateNotFoundException(code));
    }

    @Override
    public SmsTemplate getSmsTemplateByCode(String code) {
        return smsTemplateRepository.findByCode(code).orElseThrow(() -> new TemplateNotFoundException(code));
    }

    @Override
    public PushNotificationTemplate getPushTemplateByCode(String code) {
        return pushTemplateRepository.findByCode(code).orElseThrow(() -> new TemplateNotFoundException(code));
    }

}
