package com.trustai.notification_service.notification.controller;

import com.trustai.common.controller.BaseRestController;
import com.trustai.notification_service.notification.dto.InAppNotificationDto;
import com.trustai.notification_service.notification.dto.NotificationRequest;
import com.trustai.notification_service.notification.service.InAppNotificationService;
import com.trustai.notification_service.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController extends BaseRestController {
    private final NotificationService notificationService;
    private final InAppNotificationService inAppNotificationService;

    // 1️⃣ Send Notification
    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest request) {
        notificationService.send(request);
        return ResponseEntity.ok().build();
    }

    // 2️⃣ Get paginated notifications
    @GetMapping
    public Page<InAppNotificationDto> getNotifications(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        return inAppNotificationService.getUserNotifications(getCurrentUserId(), page, size, sortBy, sortDir);
    }

    // 3️⃣ Mark single notification as viewed/read
    @PatchMapping("/{id}/view")
    public ResponseEntity<Void> markAsViewed(@PathVariable Long id) {
        inAppNotificationService.markAsViewed(getCurrentUserId(), id);
        return ResponseEntity.noContent().build();
    }

    // 4️⃣ Mark multiple notifications as viewed
    @PatchMapping("/view")
    public ResponseEntity<Void> markMultipleAsViewed(@RequestBody List<Long> ids) {
        inAppNotificationService.markMultipleAsViewed(getCurrentUserId(), ids);
        return ResponseEntity.noContent().build();
    }

    // 5️⃣ Delete single notification
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        inAppNotificationService.deleteNotification(getCurrentUserId(), id);
        return ResponseEntity.noContent().build();
    }

    // 7️⃣ Delete multiple notifications
    @DeleteMapping
    public ResponseEntity<Void> deleteMultipleNotifications(@RequestBody List<Long> ids) {
        inAppNotificationService.deleteMultipleNotifications(getCurrentUserId(), ids);
        return ResponseEntity.noContent().build();
    }

}
