package com.sprint.mission.discodeit.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageResponse<T> {

  private List<T> content;
  private Integer number;
  private Integer size;
  private boolean hasNext;
  private Long totalElements;
}
