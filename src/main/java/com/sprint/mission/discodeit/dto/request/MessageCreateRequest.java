package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(

    @NotNull
    @NotEmpty
    String content,

    @NotNull
    @NotEmpty
    UUID channelId,

    @NotNull
    @NotEmpty
    UUID authorId
) {

}
