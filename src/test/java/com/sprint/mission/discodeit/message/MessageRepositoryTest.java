package com.sprint.mission.discodeit.message;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.channel.ChannelFixture;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.user.UserFixture;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;

  private Pageable pageable;

  private UUID channelId;

  private Instant createdAt;

  @BeforeEach
  public void setup() {
    messageRepository.deleteAll();

    createdAt = Instant.now().minus(5, ChronoUnit.DAYS);
    List<User> users = UserFixture.SetMockUsers(userRepository);
    List<Channel> channels = ChannelFixture.SetMockChannels(channelRepository);
    messageRepository.save(MessageFixture.createMessage("message1", channels.get(0), users.get(0)));

    channelId = channels.get(0).getId();
    pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
  }

  @Test
  @DisplayName("")
  public void testfindAllByChannelIdSuccess() {
    Slice<Message> results = messageRepository.findAllByChannelId(channelId, pageable);
    assertThat(results.getContent()).hasSize(1);
  }

  @Test
  @DisplayName("")
  public void testfindAllByChannelIdFailure() {
    Slice<Message> results = messageRepository.findAllByChannelId(UUID.randomUUID(), pageable);
    assertThat(results.getContent()).hasSize(0);
  }

  @Test
  @DisplayName("")
  public void testfindSliceAllByChannelIdAndCreatedAtAfterSuccess() {
    Slice<Message> results = messageRepository.findSliceAllByChannelIdAndCreatedAtAfter(channelId,
        createdAt, pageable);
    assertThat(results.getContent()).hasSize(1);
  }

  @Test
  @DisplayName("")
  public void testfindSliceAllByChannelIdAndCreatedAtAfterFailure() {
    Slice<Message> results = messageRepository.findSliceAllByChannelIdAndCreatedAtAfter(
        UUID.randomUUID(), createdAt, pageable);
    assertThat(results.getContent()).hasSize(0);
  }
}
