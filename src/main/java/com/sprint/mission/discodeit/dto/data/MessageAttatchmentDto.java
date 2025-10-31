package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.dto.data.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MessageAttatchmentDto {

  private UUID id;
  private BinaryContentDto attatchment;

  public MessageAttatchmentDto(MessageAttachment messageAttachment) {
    this.id = messageAttachment.getId();

    if (messageAttachment.getAttatchment() != null) {
      this.attatchment = BinaryContentMapper.INSTANCE.toDto(
          messageAttachment.getAttatchment());
    }
  }
}
