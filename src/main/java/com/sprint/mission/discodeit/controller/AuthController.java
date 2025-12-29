package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.JwtDto;
import com.sprint.mission.discodeit.dto.data.JwtInformation;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.utils.TokenUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

    private final UserService userService;
    private final AuthService authService;
    private final TokenUtils tokenUtils;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String token = csrfToken.getToken();
        log.debug("Csrf token 요청 : {}", token);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Override
    public ResponseEntity<UserDto> updateRole(UserRoleUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserRole(request));
    }

    @PostMapping("/refresh")
    @Override
    public ResponseEntity<JwtDto> refreshToken(@CookieValue(value = "REFRESH_TOKEN") String refreshToken, HttpServletResponse response) {
        JwtInformation information = authService.refreshToken(refreshToken);
        response.addCookie(tokenUtils.getRefreshCookie(information.refreshToken()));
        return ResponseEntity.status(HttpStatus.OK).body(information.dto());
    }
}
