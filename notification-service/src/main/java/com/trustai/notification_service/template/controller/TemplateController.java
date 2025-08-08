package com.trustai.notification_service.template.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
@Slf4j
public class TemplateController {
    private final TemplateControllerHelper helper;

    @GetMapping("/{type}")
    public ResponseEntity<?> getTemplates(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return helper.getTemplates(type, page, size);
    }

    @GetMapping("/{type}/{id}")
    public ResponseEntity<?> getTemplateById(@PathVariable String type, @PathVariable Long id) {
        return helper.getTemplateById(type, id);
    }

    @PutMapping("/{type}/{id}")
    public ResponseEntity<?> updateTemplate(
            @PathVariable String type,
            @PathVariable Long id,
            @RequestBody Map<String, String> updates
    ) {
        return helper.updateTemplate(type, id, updates);
    }

    @GetMapping("/{type}/code/{code}")
    public ResponseEntity<?> getTemplateByCode(@PathVariable String type, @PathVariable String code) {
        return helper.getTemplateByCode(type, code);
    }
}
