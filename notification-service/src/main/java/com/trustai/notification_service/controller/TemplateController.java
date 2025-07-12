package com.trustai.notification_service.controller;

import com.trustai.common.dto.UserInfo;
import com.trustai.notification_service.entity.EmailTemplate;
import com.trustai.notification_service.entity.PushNotificationTemplate;
import com.trustai.notification_service.entity.SmsTemplate;
import com.trustai.notification_service.service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
@Slf4j
public class TemplateController {
    private final TemplateService templateService;

    @GetMapping("/email")
    public ResponseEntity<Page<EmailTemplate>> emailTemplates(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        Page<EmailTemplate> paginatedTemplates = templateService.getEmailTemplates(page, size);
        return ResponseEntity.ok(paginatedTemplates);
    }

    @GetMapping("/sms")
    public ResponseEntity<Page<SmsTemplate>> smsTemplates(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        Page<SmsTemplate> paginatedTemplates = templateService.getSmsTemplates(page, size);
        return ResponseEntity.ok(paginatedTemplates);
    }

    @GetMapping("/push")
    public ResponseEntity<Page<PushNotificationTemplate>> pushTemplates(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        Page<PushNotificationTemplate> paginatedTemplates = templateService.getPushTemplates(page, size);
        return ResponseEntity.ok(paginatedTemplates);
    }

    @GetMapping("/email/{id}")
    public ResponseEntity<EmailTemplate> getEmailTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(templateService.getEmailTemplateById(id));
    }

    @GetMapping("/sms/{id}")
    public ResponseEntity<SmsTemplate> getSmsTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(templateService.getSmsTemplateById(id));
    }

    @GetMapping("/push/{id}")
    public ResponseEntity<PushNotificationTemplate> getPushTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(templateService.getPushTemplateById(id));
    }
}
