package com.sprint.mission.discodeit.channel;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.mapper.ChannelMapper;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private ReadStatusRepository readStatusRepository;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  UserRepository userRepository;

  @InjectMocks
  private BasicChannelService channelService;

  @Test
  @DisplayName("public 채널 생성")
  void createPublicChannelTest() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("name", "description");
    Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());

    given(channelRepository.save(any(Channel.class))).willReturn(channel);

    ChannelDto channelDto = channelService.create(request);

    assertThat(channelDto).isNotNull();
    assertThat(channelDto.getName()).isEqualTo(channel.getName());
    assertThat(channelDto.getDescription()).isEqualTo(channel.getDescription());

  }

  @Test
  @DisplayName("private 채널 생성")
  void createPrivateChannelTest() {
    UUID id = UUID.randomUUID();
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(id));
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    User user = new User("name", "test@test.com", "1234", null);

    ChannelDto channelDto = ChannelMapper.INSTANCE.toDto(channel);

    ReadStatus status = new ReadStatus(user, channel, Instant.now());
    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(userRepository.findById(id)).willReturn(Optional.of(user));
    given(readStatusRepository.save(any(ReadStatus.class))).willReturn(status);

    channelService.create(request);

    assertThat(channelDto).isNotNull();
    assertThat(channelDto.getName()).isEqualTo(channel.getName());
    assertThat(channelDto.getDescription()).isEqualTo(channel.getDescription());

  }

  @Test
  @DisplayName("업데이트 채널 테스트")
  void updateChannelTest() {
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName",
        "newDescription");
    Channel channel = new Channel(ChannelType.PUBLIC, "name", "description");
    UUID id = UUID.randomUUID();

    Channel newChannel = new Channel(ChannelType.PUBLIC, request.newName(),
        request.newDescription());

    given(channelRepository.findById(id)).willReturn(Optional.of(channel));
    given(channelRepository.save(any(Channel.class))).willReturn(newChannel);

    ChannelDto channelDto = channelService.update(id, request);
    assertThat(channelDto).isNotNull();
    assertThat(channelDto.getName()).isEqualTo(newChannel.getName());
    assertThat(channelDto.getDescription()).isEqualTo(newChannel.getDescription());
  }

  @Test
  @DisplayName("채널 삭제 테스트")
  void deleteChannelTest() {
    Channel channel = new Channel(ChannelType.PUBLIC, "name", "description");
    UUID id = UUID.randomUUID();
    given(channelRepository.findById(id)).willReturn(Optional.of(channel));
    channelService.delete(id);

    verify(channelRepository, times(1)).findById(id);
  }

  @Test
  @DisplayName("유저아이디로 채널 찾기 테스트")
  void findByUserIdChannelTest() {

    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    User user = new User("name", "test@test.com", "1234", null);
    UUID id = UUID.randomUUID();
    List<ReadStatus> statuses = List.of(new ReadStatus(user, channel, Instant.now()));
    given(readStatusRepository.findAllByUserId(id)).willReturn(Optional.of(statuses));

    List<ChannelDto> results = channelService.findAllByUserId(id);
    assertThat(results).isNotNull();
    assertThat(results.size()).isEqualTo(1);
  }
}
