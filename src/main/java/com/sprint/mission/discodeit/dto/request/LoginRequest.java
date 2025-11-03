package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
    @NotEmpty(message = "이름은 필수 입니다.")
    String username,

    @NotEmpty(message = "비밀번호는 필수 입니다.")
    String password
) {

}
