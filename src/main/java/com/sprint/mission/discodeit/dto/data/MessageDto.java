package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttatchment;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class MessageDto {

  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  private String content;
  private UUID channelId;
  private UserDto author;
  private List<MessageAttatchmentDto> attachments;

  public MessageDto(Message message) {
    this.content = message.getContent();
    this.channelId = message.getChannel().getId();
    this.author = new UserDto(message.getAuthor());
    if (message.getAttachments() != null) {
      this.attachments = message.getAttachments().stream().map(MessageAttatchmentDto::new).toList();
    }
    this.createdAt = message.getCreatedAt();
    this.updatedAt = message.getUpdatedAt();
    this.id = message.getId();
  }
}
