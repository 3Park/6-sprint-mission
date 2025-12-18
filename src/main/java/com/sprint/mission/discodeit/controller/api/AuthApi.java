package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApi {

//  @Operation(summary = "로그인")
//  @ApiResponses(value = {
//      @ApiResponse(
//          responseCode = "200", description = "로그인 성공",
//          content = @Content(schema = @Schema(implementation = UserDto.class))
//      ),
//      @ApiResponse(
//          responseCode = "404", description = "사용자를 찾을 수 없음",
//          content = @Content(examples = @ExampleObject(value = "User with username {username} not found"))
//      ),
//      @ApiResponse(
//          responseCode = "400", description = "비밀번호가 일치하지 않음",
//          content = @Content(examples = @ExampleObject(value = "Wrong password"))
//      )
//  })
//  ResponseEntity<UserDto> login(
//      @Parameter(description = "로그인 정보") LoginRequest loginRequest
//  );

  ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken);

  ResponseEntity<UserDto> me();

  ResponseEntity<UserDto> updateRole(UserRoleUpdateRequest request);
} 