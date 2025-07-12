package com.trustai.notification_service.repository;

import com.trustai.notification_service.entity.PushNotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushNotificationTemplateRepository extends JpaRepository<PushNotificationTemplate, Long> {
}
