package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PublicChannelCreateRequest(
    @NotEmpty(message = "이름은 필수 입니다.")
    String name,

    @NotEmpty(message = "설명은 필수 입니다.")
    String description
) {

}
