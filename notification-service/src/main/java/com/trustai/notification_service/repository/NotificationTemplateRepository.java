package com.trustai.notification_service.repository;

import com.trustai.notification_service.entity.NotificationTemplate;
import com.trustai.notification_service.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    Optional<NotificationTemplate> findByCodeAndNotificationType(String code, NotificationType notificationType);
}
