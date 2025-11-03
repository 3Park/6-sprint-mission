package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.mapper.ChannelMapper;
import com.sprint.mission.discodeit.dto.data.mapper.UserMapper;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.custom.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.custom.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.custom.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  //
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);
    channelRepository.save(channel);

    log.info("Creating public channel with name {} and description {}", name, description);

    return ChannelMapper.INSTANCE.toDto(channel);
  }

  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel createdChannel = channelRepository.save(channel);

    request.participantIds().stream()
        .map(userId -> new ReadStatus(userRepository.findById(userId).orElseThrow(),
            createdChannel,
            createdChannel.getCreatedAt()))
        .forEach(readStatusRepository::save);

    List<UserDto> users = request.participantIds().stream()
        .map(userId ->
        {
          User user = userRepository.findById(userId).orElseThrow();
          UserDto dto = UserMapper.INSTANCE.toDto(user);
          dto.setProfile(user.getProfile());
          return dto;
        })
        .toList();

    log.info("Creating private channel with name {} and description {}", channel.getName(),
        channel.getDescription());

    ChannelDto channelDto = ChannelMapper.INSTANCE.toDto(createdChannel);
    channelDto.setParticipants(users);

    return channelDto;
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelDto find(UUID channelId) {

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
            Map.of("channdId", channelId)));

    Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());
    Instant lastMessageAt = messageRepository.findSliceAllByChannelIdAndCreatedAtAfter(channelId,
            null, pageable)
        .stream()
        .map(Message::getCreatedAt)
        .findFirst()
        .orElse(Instant.MIN);

    List<UserDto> participantIds = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId()).orElseThrow()
          .stream()
          .map(x -> {
            User user = x.getUser();
            UserDto dto = UserMapper.INSTANCE.toDto(user);
            dto.setProfile(user.getProfile());
            return dto;
          })
          .forEach(participantIds::add);
    }

    ChannelDto dto = ChannelMapper.INSTANCE.toDto(channel);
    dto.setLastMessageAt(lastMessageAt);
    dto.setParticipants(participantIds);
    return dto;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {

    return readStatusRepository.findAllByUserId(userId).orElseThrow(
            () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId)))
        .stream()
        .map(x -> ChannelMapper.INSTANCE.toDto(x.getChannel()))
        .toList();
  }

  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
                Map.of("channdId", channelId)));
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new PrivateChannelUpdateException(ErrorCode.PRIVATE_CHANNEL_UPDATE,
          Map.of("channdId", channelId));
    }

    channel.update(newName, newDescription);
    return ChannelMapper.INSTANCE.toDto(channelRepository.save(channel));
  }

  @Override
  public void delete(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
                Map.of("channdId", channelId)));

    messageRepository.deleteAllByChannelId(channel.getId());
    readStatusRepository.deleteAllByChannelId(channel.getId());

    channelRepository.deleteById(channelId);
  }
}
