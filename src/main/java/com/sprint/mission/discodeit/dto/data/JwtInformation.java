package com.sprint.mission.discodeit.dto.data;

import lombok.Builder;

@Builder
public record JwtInformation(
        JwtDto dto,
        String refreshToken
) {
}
