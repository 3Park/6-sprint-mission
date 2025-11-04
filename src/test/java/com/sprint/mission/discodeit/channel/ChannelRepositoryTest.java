package com.sprint.mission.discodeit.channel;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  private UUID id;

  @BeforeEach
  public void setup() {
    channelRepository.deleteAll();
    List<Channel> results = ChannelFixture.SetMockChannels(channelRepository);
    id = results.get(0).getId();
  }

  @Test
  @DisplayName("findById 성공 테스트")
  public void testFindByIdSuccess() {
    Optional<Channel> result = channelRepository.findById(id);
    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getId()).isEqualTo(id);
  }

  @Test
  @DisplayName("findById 실패 테스트")
  public void testFindByIdFailure() {
    Optional<Channel> result = channelRepository.findById(UUID.randomUUID());
    assertThat(result.isPresent()).isFalse();
  }

  @Test
  @DisplayName("exsistById 성공 테스트")
  public void testExsistByIdSuccess() {
    boolean result = channelRepository.existsById(id);
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("exsistById 실패 테스트")
  public void testExsistByIdFailure() {
    boolean result = channelRepository.existsById(UUID.randomUUID());
    assertThat(result).isFalse();
  }
}
