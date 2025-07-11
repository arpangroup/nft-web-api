package com.trustai.notification_service.service;

import com.trustai.notification_service.dto.NotificationRequest;
import com.trustai.notification_service.entity.NotificationTemplate;
import com.trustai.notification_service.repository.NotificationTemplateRepository;
import com.trustai.notification_service.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationTemplateRepository templateRepo;
    private final List<NotificationStrategy> strategies;

    public void sendNotification(NotificationRequest request) {
        log.debug("Sending notification using templateCode={} and type={}", request.getTemplateCode(), request.getType());
        NotificationTemplate template = templateRepo
                .findByCodeAndNotificationType(request.getTemplateCode(), request.getType())
                .orElseThrow(() -> {
                    log.warn("Template not found for code={} and type={}", request.getTemplateCode(), request.getType());
                    return new RuntimeException("Template not found");
                });

        String subject = resolveTemplate(template.getSubject(), request.getProperties());
        String content = resolveTemplate(template.getContent(), request.getProperties());

        NotificationStrategy strategy = strategies.stream()
                .filter(s -> s.getType() == request.getType())
                .findFirst()
                .orElseThrow(() -> {
                    log.error("No strategy found for notification type: {}", request.getType());
                    return new RuntimeException("No strategy found");
                });
        log.info("Using strategy {} to send notification to {}", strategy.getClass().getSimpleName(), request.getRecipient());
        strategy.send(request.getRecipient(), subject, content);
    }

    private String resolveTemplate(String template, Map<String, String> props) {
        if (props == null) return template;
        for (Map.Entry<String, String> entry : props.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return template;
    }
}
