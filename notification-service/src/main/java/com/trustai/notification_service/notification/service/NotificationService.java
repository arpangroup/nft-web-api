package com.trustai.notification_service.notification.service;

import com.trustai.notification_service.notification.sender.NotificationSenderFactory;
import com.trustai.notification_service.dto.NotificationRequest;
import com.trustai.notification_service.template.render.TemplateRenderer;
import com.trustai.notification_service.template.service.impl.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationSenderFactory senderFactory;
    private final TemplateRenderer templateRenderer;
    private final EmailTemplateService emailTemplateService;

    public void send(NotificationRequest request) {
        if (request.getTemplateCode() != null) {
            var template = emailTemplateService.getByCode(request.getTemplateCode());
            String renderedMessage = templateRenderer.render(template.getMessageBody(), request.getProperties());
            request.setMessage(renderedMessage);
            request.setSubject(template.getSubject());
        }

        request.getChannels().forEach(channel -> {
            var sender = senderFactory.getSender(channel);
            if (sender != null) {
                sender.send(request);
            }
        });
    }
}
