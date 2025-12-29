package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final TokenUtils tokenUtils;
    
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.addCookie(tokenUtils.emptyRefreshCookie());
    }
}
