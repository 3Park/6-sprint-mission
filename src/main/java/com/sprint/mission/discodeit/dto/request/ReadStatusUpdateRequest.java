package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @NotNull(message = "날짜는 필수 입니다.")
    @Past(message = "날짜는 과거 이어야 합니다.")
    Instant newLastReadAt
) {

}
