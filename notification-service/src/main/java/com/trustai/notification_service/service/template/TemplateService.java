package com.trustai.notification_service.service.template;

import org.springframework.data.domain.Page;

import java.util.Map;

public interface TemplateService<T> {
    Page<T> getTemplates(Integer page, Integer size);
    T getTemplateById(Long id);
    T updateTemplate(Long id, Map<String, String> updates);
    T getTemplateByCode(String code);
}
