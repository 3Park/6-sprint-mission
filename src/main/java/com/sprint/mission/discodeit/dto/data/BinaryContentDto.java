package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContentDto {

  private UUID id;
  private String fileName;
  private Long size;
  private String contentType;

  public BinaryContentDto(BinaryContent binaryContent) {
    this.fileName = binaryContent.getFileName();
    this.size = binaryContent.getSize();
    this.contentType = binaryContent.getContentType();
    this.id = binaryContent.getId();
  }
}
