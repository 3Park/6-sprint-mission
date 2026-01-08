package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface NotificationApi {
    ResponseEntity<List<NotificationDto>> getNotifications();

    ResponseEntity<Void> checkNotification(UUID id);
}
