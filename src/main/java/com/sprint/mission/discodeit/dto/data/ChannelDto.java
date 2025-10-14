package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelDto {

  private UUID id;
  private String type;
  private String name;
  private String description;
  private List<UserDto> participants;
  private Instant lastMessageAt;

  public void setLastMessageAt(Instant lastMessageAt) {
    this.lastMessageAt = lastMessageAt;
  }

  public void setParticipants(List<UserDto> participants) {
    this.participants = participants;
  }
}
