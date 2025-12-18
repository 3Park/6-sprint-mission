package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.DiscodeitUserDetails;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.exception.user.InvalidCredentialsException;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;
  private final UserService userService;

//  @PostMapping(path = "login")
//  public ResponseEntity<UserDto> login(@RequestBody @Valid LoginRequest loginRequest) {
//    log.info("로그인 요청: username={}", loginRequest.username());
//    UserDto user = authService.login(loginRequest);
//    log.debug("로그인 응답: {}", user);
//    return ResponseEntity
//        .status(HttpStatus.OK)
//        .body(user);
//  }

  @GetMapping("/csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
    String token = csrfToken.getToken();
    log.debug("Csrf token 요청 : {}", token);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> me() {
    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidCredentialsException();
    }

    SecurityContext context = SecurityContextHolder.getContext();
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) context.getAuthentication()
        .getPrincipal();
    return ResponseEntity.status(HttpStatus.OK).body(userDetails.getUserDto());
  }

  @Override
  public ResponseEntity<UserDto> updateRole(UserRoleUpdateRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserRole(request));
  }
}
