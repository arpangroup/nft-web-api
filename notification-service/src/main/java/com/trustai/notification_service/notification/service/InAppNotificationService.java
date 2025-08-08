package com.trustai.notification_service.notification.service;

import com.trustai.notification_service.notification.dto.InAppNotificationDto;
import com.trustai.notification_service.notification.mapper.InAppNotificationMapper;
import com.trustai.notification_service.notification.repository.InAppNotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InAppNotificationService {
    private final InAppNotificationRepository repository;
    private final InAppNotificationMapper mapper;

    public Page<InAppNotificationDto> getUserNotifications(Long userId, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(
                page, size,
                sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        return repository.findByUserId(userId, pageable)
                .map(mapper::toDto);
    }

    @Transactional
    public void markAsViewed(Long userId, Long id) {
        repository.updateViewedStatus(userId, id, true);
    }

    @Transactional
    public void markMultipleAsViewed(Long userId, List<Long> ids) {
        repository.updateViewedStatusForIds(userId, ids, true);
    }

    @Transactional
    public void deleteNotification(Long userId, Long id) {
        repository.deleteByUserIdAndId(userId, id);
    }

    @Transactional
    public void deleteMultipleNotifications(Long userId, List<Long> ids) {
        repository.deleteByUserIdAndIdIn(userId, ids);
    }
}
