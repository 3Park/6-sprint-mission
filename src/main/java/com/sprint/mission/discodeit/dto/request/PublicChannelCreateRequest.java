package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PublicChannelCreateRequest(
    @NotNull
    @NotEmpty
    String name,

    @NotNull
    @NotEmpty
    String description
) {

}
