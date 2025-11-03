package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.exception.custom.user.UserInputDataException;
import com.sprint.mission.discodeit.exception.custom.user.UserProfileException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> create(
      @RequestPart("userCreateRequest") String req,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {

    try {
      UserCreateRequest userCreateRequest = new ObjectMapper().readValue(req,
          UserCreateRequest.class);

      Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
          .flatMap(this::resolveProfileRequest);
      UserDto createdUser = userService.create(userCreateRequest, profileRequest);
      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(createdUser);
    } catch (JsonProcessingException e) {
      throw new UserInputDataException(ErrorCode.INVALID_USER_DATA, Map.of("request", req));
    }
  }

  @PatchMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> update(
      @PathVariable("userId") UUID userId,
      @RequestPart("userUpdateRequest") String req,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    try {
      UserUpdateRequest userUpdateRequest = new ObjectMapper().readValue(req,
          UserUpdateRequest.class);

      Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
          .flatMap(this::resolveProfileRequest);
      UserDto updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(updatedUser);
    } catch (JsonProcessingException e) {
      throw new UserInputDataException(ErrorCode.INVALID_USER_DATA, Map.of("request", req));
    }
  }

  @DeleteMapping(value = "/{userId}")
  public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
    userService.delete(userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(users);
  }

  @PatchMapping(value = "/{userId}/userStatus")
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(@PathVariable("userId") UUID userId,
      @RequestBody @Valid UserStatusUpdateRequest request) {
    UserStatusDto updatedUserStatus = userStatusService.updateByUserId(userId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUserStatus);
  }

  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new UserProfileException(ErrorCode.INVALID_PROFILE_DATA,
            Map.of("request", profileFile));
      }
    }
  }
}
