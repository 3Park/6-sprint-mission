package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.PageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public class PageResponseMapper {

  public static <T extends PageDto> PageResponse<T> fromSlice(Slice<T> slice) {
    PageResponse<T> response = new PageResponse<>();
    response.setContent(slice.getContent());
    response.setSize(slice.getSize());
    response.setHasNext(slice.hasNext());
    response.setTotalElements(null);

    if (slice.hasNext() && slice.getContent().isEmpty() == false) {
      T lastItem = slice.getContent().get(slice.getContent().size() - 1);
      response.setNextCursor(lastItem.getCreatedAt()); // last item's cursor
    } else {
      response.setNextCursor(null);
    }

    return response;
  }

  public static <T> PageResponse<T> fromPage(Page<T> page) {
    PageResponse<T> response = new PageResponse<>();
    response.setContent(page.getContent());
    response.setNextCursor(page.getNumber());
    response.setSize(page.getSize());
    response.setHasNext(page.hasNext());
    response.setTotalElements(page.getTotalElements());
    return response;
  }
}
