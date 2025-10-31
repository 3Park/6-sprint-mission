package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
    @NotNull
    @NotEmpty
    String username,

    @NotNull
    @NotEmpty
    @Email
    String email,

    @NotNull
    @NotEmpty
    String password
) {

}
