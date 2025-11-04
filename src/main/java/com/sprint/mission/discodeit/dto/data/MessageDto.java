package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.MessageAttachment;
import java.time.Instant;
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

  public void setAttachments(List<MessageAttachment> messageAttachments) {
    this.attachments = Optional.ofNullable(messageAttachments)
        .map(list -> list.stream()
            .map(MessageAttatchmentDto::new)
            .toList())
        .orElse(null);
  }
}
