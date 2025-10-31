package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BinaryContentCreateRequest(
    @NotNull
    @NotEmpty
    String fileName,

    @NotNull
    @NotEmpty
    String contentType,

    @NotNull
    @NotEmpty
    byte[] bytes
) {

}
