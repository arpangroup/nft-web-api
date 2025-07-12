package com.trustai.notification_service.exception;

public class TemplateNotFoundException extends RuntimeException{

    public TemplateNotFoundException(String code) {
        super("Template code=" + code + " not found");
    }

    public TemplateNotFoundException(Long id) {
        super("Template Id=" + id + " not found");
    }
}
