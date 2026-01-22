package com.sprint.mission.discodeit.service.basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SecurityContextUtils securityContextUtils;
    private final NotificationMapper notificationMapper;
    private final CachedNotificationService cachedNotificationService;
    private final ObjectMapper objectMapper;

    @Override
    public List<NotificationDto> findByReceiverId() {
        String result = cachedNotificationService.findByReceiverId();
        if (StringUtils.isEmpty(result))
            return List.of();

        try {
            return objectMapper.readValue(result, new TypeReference<List<NotificationDto>>() {
            });
        } catch (JsonProcessingException e) {
            throw new NotificationException(ErrorCode.INVALID_REQUEST);
        }
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
