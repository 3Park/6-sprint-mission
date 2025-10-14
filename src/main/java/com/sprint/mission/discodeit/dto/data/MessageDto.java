package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttatchment;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto extends PageDto {

  private UUID id;
  private Instant updatedAt;
  private String content;
  private UUID channelId;
  private UserDto author;
  private List<MessageAttatchmentDto> attachments;

  public void setAttachments(List<MessageAttatchment> messageAttatchments) {
    this.attachments = Optional.of(messageAttatchments
            .stream()
            .map(MessageAttatchmentDto::new).
            toList())
        .orElse(null);
  }
}
