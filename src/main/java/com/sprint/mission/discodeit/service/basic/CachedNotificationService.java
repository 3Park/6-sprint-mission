package com.sprint.mission.discodeit.service.basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.notification.NotificationException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CachedNotificationService {

    private final NotificationRepository notificationRepository;
    private final SecurityContextUtils securityContextUtils;
    private final NotificationMapper notificationMapper;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    @Cacheable(
            value = "notifications",
            key = "@SecurityContext.getUserId()",
            unless = "#result.length() == 0"
    )
    public String findByReceiverId() {
        List<NotificationDto> items = notificationRepository.findByReceiverId(securityContextUtils.getUserId()).orElse(List.of())
                .stream()
                .map(notificationMapper::toDto)
                .toList();

        if (items.isEmpty())
            return "";

        try {
            return objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            throw new NotificationException(ErrorCode.INVALID_REQUEST);
        }
    }
}
