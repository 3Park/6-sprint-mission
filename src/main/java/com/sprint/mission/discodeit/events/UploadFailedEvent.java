package com.sprint.mission.discodeit.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UploadFailedEvent {
    private final String title;
    private final String content;
}
