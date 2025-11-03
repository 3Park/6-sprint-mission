package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.data.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.custom.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.custom.readstatus.ReadStatusAlreadyExsistException;
import com.sprint.mission.discodeit.exception.custom.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.custom.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
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
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatusDto create(ReadStatusCreateRequest request) {
    UUID userId = request.userId();
    UUID channelId = request.channelId();

    User user = userRepository.findById(userId).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId)));
    Channel channel = channelRepository.findById(channelId).orElseThrow(
        () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
            Map.of("channelId", channelId)));

    if (readStatusRepository.findAllByUserId(userId).orElseThrow().stream()
        .anyMatch(readStatus -> readStatus.getChannel().getId().equals(channelId))) {
      throw new ReadStatusAlreadyExsistException(ErrorCode.DUPLICATE_READSTATUS,
          Map.of("channelId", channelId, "userId", userId));
    }

    Instant lastReadAt = request.lastReadAt();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    return ReadStatusMapper.INSTANCE.toDto(readStatusRepository.save(readStatus));
  }

  @Override
  @Transactional(readOnly = true)
  public ReadStatusDto find(UUID readStatusId) {
    ReadStatus status = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new ReadStatusNotFoundException(ErrorCode.READSTATUS_NOT_FOUND,
                Map.of("readStatusId", readStatusId)));
    return ReadStatusMapper.INSTANCE.toDto(status);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).orElseThrow(
            () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId)))
        .stream()
        .map(ReadStatusMapper.INSTANCE::toDto)
        .toList();
  }

  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    Instant newLastReadAt = request.newLastReadAt();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new ReadStatusNotFoundException(ErrorCode.READSTATUS_NOT_FOUND,
                Map.of("readStatusId", readStatusId)));
    readStatus.update(newLastReadAt);
    return ReadStatusMapper.INSTANCE.toDto(readStatusRepository.save(readStatus));
  }

  @Override
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new ReadStatusNotFoundException(ErrorCode.READSTATUS_NOT_FOUND,
          Map.of("readStatusId", readStatusId));
    }
    readStatusRepository.deleteById(readStatusId);
  }
}
