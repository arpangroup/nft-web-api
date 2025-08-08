package com.trustai.notification_service.template.render;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class TemplateRenderer {
    public String render(String template, Map<String, String> props) {
        log.info("render template.....");
        if (props == null) return template;
        for (Map.Entry<String, String> entry : props.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return template;
    }
}
