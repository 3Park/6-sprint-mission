package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.mapper.UserMapper;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.custom.user.UserAlreadyExsistsException;
import com.sprint.mission.discodeit.exception.custom.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  //
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    if (userRepository.existsByEmail(email)) {
      throw new UserAlreadyExsistsException(ErrorCode.DUPLICATE_USER, Map.of("email", email));
    }
    if (userRepository.existsByUsername(username)) {
      throw new UserAlreadyExsistsException(ErrorCode.DUPLICATE_USER, Map.of("username", username));
    }

    BinaryContent nullableProfileId = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);

          BinaryContent created = binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(created.getId(), bytes);
          return created;
        })
        .orElse(null);
    String password = userCreateRequest.password();

    User user = new User(username, email, password, nullableProfileId);
    User createdUser = userRepository.save(user);

    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(createdUser, now);
    userStatusRepository.save(userStatus);

    UserDto dto = UserMapper.INSTANCE.toDto(createdUser);
    dto.setProfile(createdUser.getProfile());

    log.info("Created user : {}", createdUser);

    return dto;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto find(UUID userId) {

    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("id", userId)));

    UserDto dto = UserMapper.INSTANCE.toDto(user);
    dto.setProfile(user.getProfile());
    return dto;
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(x -> {
          UserDto dto = UserMapper.INSTANCE.toDto(x);
          dto.setProfile(x.getProfile());
          return dto;
        })
        .toList();
  }

  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("id", userId)));

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (userRepository.existsByEmail(newEmail)) {
      throw new UserAlreadyExsistsException(ErrorCode.DUPLICATE_USER, Map.of("email", newEmail));
    }
    if (userRepository.existsByUsername(newUsername)) {
      throw new UserAlreadyExsistsException(ErrorCode.DUPLICATE_USER,
          Map.of("username", newUsername));
    }

    BinaryContent nullableProfileId = optionalProfileCreateRequest
        .map(profileRequest -> {
          Optional.ofNullable(user.getProfile())
              .ifPresent(x -> {
                binaryContentRepository.deleteById(x.getId());
                binaryContentStorage.delete(x.getId());
              });

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);

          BinaryContent created = binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(created.getId(), bytes);
          return created;
        })
        .orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, nullableProfileId);

    UserDto dto = UserMapper.INSTANCE.toDto(user);
    dto.setProfile(user.getProfile());
    return dto;
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("id", userId)));

    Optional.ofNullable(user.getProfile())
        .ifPresent(x ->
        {
          binaryContentRepository.deleteById(x.getId());
          binaryContentStorage.delete(x.getId());
        });
    userStatusRepository.deleteByUserId(userId);

    userRepository.deleteById(userId);
  }
}
