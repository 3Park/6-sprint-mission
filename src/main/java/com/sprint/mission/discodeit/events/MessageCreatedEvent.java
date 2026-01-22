package com.sprint.mission.discodeit.events;


import com.sprint.mission.discodeit.entity.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class MessageCreatedEvent {
    private Channel channel;
    private UUID authorId;
    private String title;
    private String content;
}
