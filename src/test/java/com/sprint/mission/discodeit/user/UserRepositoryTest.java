package com.sprint.mission.discodeit.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  public void setup() {
    UserFixture.SetMockUsers(userRepository);
  }

  @Test
  @DisplayName("이메일 있는지 여부 성공")
  void exsitUserByEmailSuccess() {
    boolean result = userRepository.existsByEmail("test1@test.com");
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("이메일 있는지 여부 실패")
  void exsitUserByEmailFailure() {
    boolean result = userRepository.existsByEmail("test1");
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("이름 있는지 여부 성공")
  void exsitUserByNameSuccess() {
    boolean result = userRepository.existsByUsername("test1");
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("이름 있는지 여부 실패")
  void exsitUserByNameFailure() {
    boolean result = userRepository.existsByUsername("t");
    assertThat(result).isFalse();
  }
}
