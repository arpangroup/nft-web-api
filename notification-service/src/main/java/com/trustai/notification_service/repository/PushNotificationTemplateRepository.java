package com.trustai.notification_service.repository;

import com.trustai.notification_service.entity.PushNotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PushNotificationTemplateRepository extends JpaRepository<PushNotificationTemplate, Long> {
    Optional<PushNotificationTemplate> findByCode(String code);
}
