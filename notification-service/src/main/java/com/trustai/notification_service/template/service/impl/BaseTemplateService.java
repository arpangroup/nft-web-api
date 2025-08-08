package com.trustai.notification_service.template.service.impl;

import com.trustai.notification_service.exception.TemplateNotFoundException;
import com.trustai.notification_service.template.entity.Template;
import com.trustai.notification_service.template.repository.CodeBasedTemplateRepository;
import com.trustai.notification_service.template.service.TemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;
import java.util.Optional;

public abstract class BaseTemplateService<T extends Template, R extends CodeBasedTemplateRepository<T>> implements TemplateService<T> {
    protected final R repository;

    protected BaseTemplateService(R repository) {
        this.repository = repository;
    }

    @Override
    public Page<T> getTemplates(Integer page, Integer size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Override
    public T getTemplateById(Long id) {
        return repository.findById(id).orElseThrow(() -> new TemplateNotFoundException(id));
    }

    @Override
    public Optional<T> findByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public T getByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new TemplateNotFoundException(code));
    };


    @Override
    public T updateTemplate(Long id, Map<String, String> updates) {
        T template = repository.findById(id).orElseThrow(() -> new TemplateNotFoundException(id));
        template.updateFields(updates);
        return repository.save(template);
    }
}
