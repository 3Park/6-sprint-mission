package com.sprint.mission.discodeit.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.DiscodeitUserDetails;
import com.sprint.mission.discodeit.dto.data.JwtDto;
import com.sprint.mission.discodeit.dto.data.JwtInformation;
import com.sprint.mission.discodeit.jwt.JwtProperties;
import com.sprint.mission.discodeit.jwt.JwtRegistry;
import com.sprint.mission.discodeit.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.utils.TokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenUtils tokenUtils;
    private final JwtRegistry jwtRegistry;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        DiscodeitUserDetails user = (DiscodeitUserDetails) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getUserDto().role().name());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername(), user.getUserDto().role().name());

        response.addCookie(tokenUtils.getRefreshCookie(refreshToken));

        JwtDto dto = JwtDto.builder()
                .userDto(user.getUserDto())
                .accessToken(accessToken)
                .build();

        JwtInformation information = JwtInformation.builder()
                .dto(dto)
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();

        jwtRegistry.registerJwtInformation(information);

        response.getWriter().write(objectMapper.writeValueAsString(dto));
    }
}
