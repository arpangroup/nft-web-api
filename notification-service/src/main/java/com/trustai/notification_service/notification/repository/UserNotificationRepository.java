package com.trustai.notification_service.notification.repository;

import com.trustai.notification_service.notification.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    List<UserNotification> findByUserIdOrderByViewedAscCreatedAtDesc(Long userId);
}
