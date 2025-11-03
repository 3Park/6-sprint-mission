package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.data.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.custom.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.custom.userstatus.UserStatusAlreadyExsistException;
import com.sprint.mission.discodeit.exception.custom.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public UserStatusDto create(UserStatusCreateRequest request) {
    UUID userId = request.userId();
    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("id", userId)));

    if (userStatusRepository.findByUserId(userId).isPresent()) {
      throw new UserStatusAlreadyExsistException(ErrorCode.DUPLICATE_USERSTATUS,
          Map.of("id", userId));
    }

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    return UserStatusMapper.INSTANCE.toDto(userStatusRepository.save(userStatus));
  }

  @Override
  @Transactional(readOnly = true)
  public UserStatusDto find(UUID userStatusId) {
    UserStatus staus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new UserStatusNotFoundException(ErrorCode.USERSTATUS_NOT_FOUND,
                Map.of("id", userStatusId)));

    return UserStatusMapper.INSTANCE.toDto(userStatusRepository.save(staus));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(UserStatusMapper.INSTANCE::toDto)
        .toList();
  }

  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new UserStatusNotFoundException(ErrorCode.USERSTATUS_NOT_FOUND,
                Map.of("id", userStatusId)));
    userStatus.update(newLastActiveAt);

    return UserStatusMapper.INSTANCE.toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new UserStatusNotFoundException(ErrorCode.USERSTATUS_NOT_FOUND,
                Map.of("id", userId)));
    userStatus.update(newLastActiveAt);

    return UserStatusMapper.INSTANCE.toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      throw new UserStatusNotFoundException(ErrorCode.USERSTATUS_NOT_FOUND,
          Map.of("id", userStatusId));
    }
    userStatusRepository.deleteById(userStatusId);
  }
}
