package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(

    @NotEmpty(message = "참여자 아이디는 필수 입니다.")
    List<UUID> participantIds
) {

}
