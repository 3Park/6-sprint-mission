package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotEmpty(message = "ID는 필수 입니다.")
    UUID userId,

    @NotEmpty(message = "channelId 는 필수 입니다.")
    UUID channelId,

    @NotEmpty(message = "날짜는 필수 입니다.")
    @Past(message = "날짜는 과거 이어야 합니다.")
    Instant lastReadAt
) {

}
