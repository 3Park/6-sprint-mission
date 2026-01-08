package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.DiscodeitUserDetails;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.notification.NotificationException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final SecurityContextUtils securityContextUtils;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "notifications",
            key = "@SecurityContext.getUserId()"
    )
    public List<NotificationDto> findByReceiverId() {
        return notificationRepository.findByReceiverId(securityContextUtils.getUserId()).orElse(List.of())
                .stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    @CacheEvict(
            value = "notifications",
            allEntries = true
    )
    public void saveAllNotifications(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    @Override
    @CacheEvict(
            value = "notifications",
            key = "#notification.receiverId"
    )
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    @CacheEvict(
            value = "notifications",
            allEntries = true
    )
    public void deleteById(UUID id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> new NotificationException(ErrorCode.NOTIFICATION_NOT_FOUND)
        );

        if (securityContextUtils.getUserId() != notification.getReceiverId()) {
            throw new NotificationException(ErrorCode.INVALID_USER_AUTHORIZATION);
        }

        notificationRepository.delete(notification);
    }
}
