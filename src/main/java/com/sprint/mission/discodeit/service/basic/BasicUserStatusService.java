package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.data.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
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
        .orElseThrow(() -> new NoSuchElementException("User not found"));

    if (userStatusRepository.findByUserId(userId).isPresent()) {
      throw new IllegalArgumentException("UserStatus with id " + userId + " already exists");
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
            () -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));

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
            () -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
    userStatus.update(newLastActiveAt);

    return UserStatusMapper.INSTANCE.toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with userId " + userId + " not found"));
    userStatus.update(newLastActiveAt);

    return UserStatusMapper.INSTANCE.toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      throw new NoSuchElementException("UserStatus with id " + userStatusId + " not found");
    }
    userStatusRepository.deleteById(userStatusId);
  }
}
