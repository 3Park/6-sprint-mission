package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.dto.data.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.entity.MessageAttatchment;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MessageAttatchmentDto {

  private UUID id;
  private BinaryContentDto attatchment;

  public MessageAttatchmentDto(MessageAttatchment messageAttatchment) {
    this.id = messageAttatchment.getId();

    if (messageAttatchment.getAttatchment() != null) {
      this.attatchment = BinaryContentMapper.INSTANCE.toDto(
          messageAttatchment.getAttatchment());
    }
  }
}
