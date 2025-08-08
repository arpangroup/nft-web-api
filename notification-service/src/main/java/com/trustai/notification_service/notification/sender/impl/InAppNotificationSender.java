package com.trustai.notification_service.notification.sender.impl;

import com.trustai.notification_service.dto.NotificationRequest;
import com.trustai.notification_service.notification.enums.NotificationChannel;
import com.trustai.notification_service.notification.sender.NotificationSender;

public class InAppNotificationSender implements NotificationSender {

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.IN_APP;
    }

    @Override
    public void send(NotificationRequest request) {
        // TODO Save to DB logic for in-app notification...
//        Notification notification = new Notification(...);
//        notification.setMessage(request.getMessage());
//        notification.setTitle(request.getTitle());
//        notification.setCreatedAt(LocalDateTime.now());
//
//        notificationRepo.save(notification);
//
//        List<Long> targetUserIds = request.isSendToAll()
//                ? userNotificationRepo.findAllActiveUserIds() // method to implement
//                : List.of(Long.parseLong(request.getRecipient()));
//
//        targetUserIds.forEach(uid ->
//                userNotificationRepo.save(new UserNotification(null, notification, uid, false, null, LocalDateTime.now())));
    }
}
