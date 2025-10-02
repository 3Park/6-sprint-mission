package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
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
  private List<BinaryContent> attachments;

  public MessageDto(Message message) {
    this.content = message.getContent();
    this.channelId = message.getChannel().getId();
    this.author = new UserDto(message.getAuthor());
    this.attachments = message.getAttachments();
    this.createdAt = message.getCreatedAt();
    this.updatedAt = message.getUpdatedAt();
    this.id = message.getId();
  }
}
