package com.trustai.notification_service.service.template;

import com.trustai.notification_service.entity.PushNotificationTemplate;
import com.trustai.notification_service.exception.TemplateNotFoundException;
import com.trustai.notification_service.repository.PushNotificationTemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class PushNotificationTemplateService extends BaseTemplateService<PushNotificationTemplate, PushNotificationTemplateRepository> {
    private final PushNotificationTemplateRepository repository;

    protected PushNotificationTemplateService(PushNotificationTemplateRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public PushNotificationTemplate getTemplateByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new TemplateNotFoundException(code));
    }

    @Override
    public PushNotificationTemplate updateTemplate(Long id, Map<String, String> updates) {
        PushNotificationTemplate template = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PushNotificationTemplate not found with id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "code" -> template.setCode(value);
                case "title" -> template.setTitle(value);
                case "messageBody" -> template.setMessageBody(value);
                case "templateFor" -> template.setTemplateFor(value);
                case "templateActive" -> template.setTemplateActive(Boolean.parseBoolean(value));
                default -> throw new IllegalArgumentException("Unknown field: " + key);
            }
        });

        return repository.save(template);
    }
}
