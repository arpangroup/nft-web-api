package com.trustai.notification_service.service.template;

import com.trustai.notification_service.exception.TemplateNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public abstract class BaseTemplateService<T, R extends JpaRepository<T, Long>> implements TemplateService<T> {
    protected final R repository;

    protected BaseTemplateService(R repository) {
        this.repository = repository;
    }

    @Override
    public Page<T> getTemplates(Integer page, Integer size) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    public T getTemplateById(Long id) {
        return repository.findById(id).orElseThrow(() -> new TemplateNotFoundException(id));
    }

    public abstract T getTemplateByCode(String code);

    public abstract T updateTemplate(Long id, Map<String, String> updates);
}
