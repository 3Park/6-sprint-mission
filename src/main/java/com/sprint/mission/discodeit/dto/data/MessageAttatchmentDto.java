package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttatchment;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

public class MessageAttatchmentDto {

  private UUID id;
  private MessageDto message;
  private BinaryContentDto attatchment;
  private OffsetDateTime createAt;

  public MessageAttatchmentDto(MessageAttatchment messageAttatchment) {
    this.id = messageAttatchment.getId();

    if (messageAttatchment.getMessage() != null) {
      this.message = new MessageDto(messageAttatchment.getMessage());
    }

    if (messageAttatchment.getAttatchment() != null) {
      this.attatchment = new BinaryContentDto(messageAttatchment.getAttatchment());
    }
  }
}
