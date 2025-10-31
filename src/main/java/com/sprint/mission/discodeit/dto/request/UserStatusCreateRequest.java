package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    @NotNull
    @NotEmpty
    UUID userId,

    @NotNull
    @NotEmpty
    Instant lastActiveAt
) {

}
