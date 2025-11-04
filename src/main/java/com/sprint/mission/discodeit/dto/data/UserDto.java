package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.dto.data.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

  private UUID id;
  private String username;
  private String email;
  private BinaryContentDto profile;
  private Boolean online = false;

  public void setProfile(BinaryContent profile) {
    if (profile == null) {
      return;
    }

    this.profile = Optional.ofNullable(BinaryContentMapper.INSTANCE.toDto(profile)).orElse(null);
  }

  public void update(Boolean online) {
    this.online = online;
  }
}
