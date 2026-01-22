package com.sprint.mission.discodeit.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class RoleUpdatedEvent {
    private final UUID receiverId;
    private final String title;
    private final String content;
}
