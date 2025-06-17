package com.trustai.user_service.user.repository;

import com.trustai.user_service.user.entity.PendingRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingRegistrationRepository extends JpaRepository<PendingRegistration, Long> {
}
