package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReadStatusDto {

  private UUID id;
  private UUID userId;
  private UUID channelId;
  private Instant lastReadAt;

  public ReadStatusDto(ReadStatus readStatus) {
    this.userId = readStatus.getUser().getId();
    this.channelId = readStatus.getChannel().getId();
    this.lastReadAt = readStatus.getLastReadAt();
    this.id = readStatus.getId();
  }
}
