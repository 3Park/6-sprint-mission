package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(

    @NotEmpty(message = "내용은 필수 입니다.")
    String content,

    @NotEmpty(message = "채널 아이디는 필수 입니다.")
    UUID channelId,

    @NotEmpty(message = "유저 아이디는 필수 입니다.")
    UUID authorId
) {

}
