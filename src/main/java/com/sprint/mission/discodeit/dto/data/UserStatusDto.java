package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatusDto {

  private UUID id;
  private UUID userId;
  private Instant lastActiveAt;

  public UserStatusDto(User user, Instant lastActiveAt) {
    this.userId = user.getId();
    this.lastActiveAt = lastActiveAt;
    this.id = user.getId();
  }

  public UserStatusDto(UserStatus userStatus) {
    this.userId = userStatus.getUser().getId();
    this.lastActiveAt = userStatus.getLastActiveAt();
  }
}
