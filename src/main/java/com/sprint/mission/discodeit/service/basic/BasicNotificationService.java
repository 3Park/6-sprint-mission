package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.DiscodeitUserDetails;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.notification.NotificationException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
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

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> findByReceiverId() {
        return notificationRepository.findByReceiverId(getUserId()).orElse(List.of())
                .stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void saveAllNotifications(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> new NotificationException(ErrorCode.NOTIFICATION_NOT_FOUND)
        );

        if (getUserId() != notification.getReceiverId()) {
            throw new NotificationException(ErrorCode.INVALID_USER_AUTHORIZATION);
        }

        notificationRepository.delete(notification);
    }

    private UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || authentication.getDetails() instanceof DiscodeitUserDetails)
            throw new NotificationException(ErrorCode.INVALID_USER_CREDENTIALS);

        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getDetails();
        return userDetails.getUserId();
    }
}
