package com.trustai.notification_service.controller;

import com.trustai.notification_service.dto.NotificationPublishRequest;
import com.trustai.notification_service.entity.Notification;
import com.trustai.notification_service.entity.UserNotification;
import com.trustai.notification_service.service.NotificationServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationControllerV1 {
    private final NotificationServiceV1 notificationService;

    @PostMapping("/publish")
    public void publish(@RequestBody NotificationPublishRequest req) {
        Notification notif = new Notification();
        notif.setTitle(req.getTitle());
        notif.setMessage(req.getMessage());
        notif.setGlobal(req.isGlobal());
        notificationService.publish(notif, req.getUserIds());
    }

    @GetMapping
    public List<UserNotification> getNotifications(@RequestParam Long userId) {
        return notificationService.getUserNotifications(userId);
    }

    @PostMapping("/mark-viewed/{id}")
    public void markViewed(@PathVariable Long id, @RequestParam Long userId) {
        notificationService.markAsViewed(userId, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @RequestParam Long userId) {
        notificationService.delete(userId, id);
    }
}
