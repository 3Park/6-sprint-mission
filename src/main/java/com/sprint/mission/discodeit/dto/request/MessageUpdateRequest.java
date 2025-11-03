package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record MessageUpdateRequest(
    @NotEmpty(message = "값은 필수 입니다.")
    String newContent
) {

}
