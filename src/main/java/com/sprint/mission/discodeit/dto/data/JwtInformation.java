package com.sprint.mission.discodeit.dto.data;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtInformation {

    private JwtDto dto;
    private String accessToken;
    private String refreshToken;

    @Builder
    public JwtInformation(JwtDto dto, String accessToken, String refreshToken) {
        this.dto = dto;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void rotate(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
