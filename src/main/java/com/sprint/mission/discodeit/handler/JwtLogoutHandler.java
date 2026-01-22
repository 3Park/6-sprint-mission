package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.jwt.JwtRegistry;
import com.sprint.mission.discodeit.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final TokenUtils tokenUtils;
    private final JwtRegistry jwtRegistry;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(TokenUtils.REFRESH_TOKEN))
                .findFirst()
                .ifPresent(cookie -> {
                    jwtRegistry.invalidateJwtInformationByRefreshToken(cookie.getValue());
                });
        response.addCookie(tokenUtils.emptyRefreshCookie());
    }
}
