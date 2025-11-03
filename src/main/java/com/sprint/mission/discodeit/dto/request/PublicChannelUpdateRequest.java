package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import org.springdoc.api.ErrorMessage;

public record PublicChannelUpdateRequest(
    String newName,
    String newDescription
) {

}
