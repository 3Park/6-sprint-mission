package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinaryContentDto {

  private UUID id;
  private String fileName;
  private Long size;
  private String contentType;


}
