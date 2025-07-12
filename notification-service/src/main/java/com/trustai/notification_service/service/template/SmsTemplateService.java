package com.trustai.notification_service.service.template;

import com.trustai.notification_service.entity.SmsTemplate;
import com.trustai.notification_service.exception.TemplateNotFoundException;
import com.trustai.notification_service.repository.SmsTemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class SmsTemplateService extends BaseTemplateService<SmsTemplate, SmsTemplateRepository> {
    private final SmsTemplateRepository repository;

    protected SmsTemplateService(SmsTemplateRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public SmsTemplate getTemplateByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new TemplateNotFoundException(code));
    }

    @Override
    public SmsTemplate updateTemplate(Long id, Map<String, String> updates) {
        SmsTemplate template = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SmsTemplate not found with id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "code" -> template.setCode(value);
                case "messageBody" -> template.setMessageBody(value);
                case "templateFor" -> template.setTemplateFor(value);
                case "templateActive" -> template.setTemplateActive(Boolean.parseBoolean(value));
                default -> throw new IllegalArgumentException("Unknown field: " + key);
            }
        });

        return repository.save(template);
    }
}
