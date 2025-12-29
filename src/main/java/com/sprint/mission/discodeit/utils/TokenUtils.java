package com.sprint.mission.discodeit.utils;

import com.sprint.mission.discodeit.jwt.JwtProperties;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenUtils {

    private final JwtProperties jwtProperties;

    public Cookie getRefreshCookie(String refreshToken) {
        Cookie cookie = new Cookie("REFRESH_TOKEN", refreshToken);
        cookie.setPath("/");
        cookie.setMaxAge(Math.toIntExact(jwtProperties.getRefreshKeyExpiration()));
        cookie.setHttpOnly(true);
        return cookie;
    }

    public Cookie emptyRefreshCookie() {
        Cookie cookie = new Cookie("REFRESH_TOKEN", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
