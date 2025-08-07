package com.trustai.notification_service.service;

import com.trustai.notification_service.entity.Notification;
import com.trustai.notification_service.entity.UserNotification;
import com.trustai.notification_service.repository.NotificationRepository;
import com.trustai.notification_service.repository.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceV1 {
    private final NotificationRepository notificationRepo;
    private final UserNotificationRepository userNotificationRepo;

    public void publish(Notification notification, List<Long> userIds) {
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepo.save(notification);
        if (notification.isGlobal()) {
            // Assume userIds contains all active userIds
            userIds.forEach(uid -> userNotificationRepo.save(
                    new UserNotification(null, notification, uid, false, null, LocalDateTime.now())));
        } else {
            userIds.forEach(uid -> userNotificationRepo.save(
                    new UserNotification(null, notification, uid, false, null, LocalDateTime.now())));
        }
    }

    public List<UserNotification> getUserNotifications(Long userId) {
        return userNotificationRepo.findByUserIdOrderByViewedAscCreatedAtDesc(userId);
    }

    public void markAsViewed(Long userId, Long userNotificationId) {
        userNotificationRepo.findById(userNotificationId).ifPresent(un -> {
            if (un.getUserId().equals(userId)) {
                un.setViewed(true);
                un.setViewedAt(LocalDateTime.now());
                userNotificationRepo.save(un);
            }
        });
    }

    public void delete(Long userId, Long userNotificationId) {
        userNotificationRepo.findById(userNotificationId).ifPresent(un -> {
            if (un.getUserId().equals(userId)) userNotificationRepo.delete(un);
        });
    }
}
