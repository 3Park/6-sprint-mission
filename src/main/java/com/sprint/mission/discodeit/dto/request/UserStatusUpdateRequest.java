package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.Instant;

public record UserStatusUpdateRequest(
    @NotNull(message = "시간은 null 일 수 없습니다.")
    @Past(message = "시간은 과거 이어야 합니다.")
    Instant newLastActiveAt
) {

}
