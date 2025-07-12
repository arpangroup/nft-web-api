package com.trustai.notification_service.service.template;

import com.trustai.notification_service.entity.EmailTemplate;
import com.trustai.notification_service.exception.TemplateNotFoundException;
import com.trustai.notification_service.repository.EmailTemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class EmailTemplateService extends BaseTemplateService<EmailTemplate, EmailTemplateRepository> {
    private final EmailTemplateRepository repository;

    protected EmailTemplateService(EmailTemplateRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public EmailTemplate getTemplateByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new TemplateNotFoundException(code));
    }

    @Override
    public EmailTemplate updateTemplate(Long id, Map<String, String> updates) {
        EmailTemplate template = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EmailTemplate not found with id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "code" -> template.setCode(value);
                case "subject" -> template.setSubject(value);
                case "banner" -> template.setBanner(value);
                case "title" -> template.setTitle(value);
                case "salutation" -> template.setSalutation(value);
                case "messageBody" -> template.setMessageBody(value);
                case "buttonLevel" -> template.setButtonLevel(value);
                case "buttonLink" -> template.setButtonLink(value);
                case "enableFooterStatus" -> template.setEnableFooterStatus(Boolean.parseBoolean(value));
                case "footerBody" -> template.setFooterBody(value);
                case "enableFooterBottom" -> template.setEnableFooterBottom(Boolean.parseBoolean(value));
                case "bottomTitle" -> template.setBottomTitle(value);
                case "bottomBody" -> template.setBottomBody(value);
                case "templateFor" -> template.setTemplateFor(value);
                case "templateActive" -> template.setTemplateActive(Boolean.parseBoolean(value));
                default -> throw new IllegalArgumentException("Unknown field: " + key);
            }
        });

        return repository.save(template);
    }
}
