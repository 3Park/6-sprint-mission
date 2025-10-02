package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ChannelDto {

  private UUID id;
  private ChannelType type;
  private String name;
  private String description;
  private List<UserDto> participantIds;
  private Instant lastMessageAt;

  public ChannelDto(Channel channel) {
    this.id = channel.getId();
    this.type = channel.getType();
    this.name = channel.getName();
    this.description = channel.getDescription();
    this.lastMessageAt = channel.getUpdatedAt();
  }

  public void setLastMessageAt(Instant lastMessageAt) {
    this.lastMessageAt = lastMessageAt;
  }

  public void setParticipantIds(List<UserDto> participantIds) {
    this.participantIds = participantIds;
  }
}
