package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadStatusDto {

  private UUID id;
  private UUID userId;
  private UUID channelId;
  private Instant lastReadAt;
}
