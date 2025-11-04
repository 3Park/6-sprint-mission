package com.sprint.mission.discodeit.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

@ExtendWith(MockitoExtension.class)
@Import({BasicUserService.class})
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserStatusRepository userStatusRepository;

  @InjectMocks
  private BasicUserService userService;

  @Test
  @DisplayName("유저 생성 테스트")
  void createTestSuccess() {
    UserCreateRequest request = new UserCreateRequest("name", "test@test.com", "1234");
    User saved = new User(request.username(), request.email(), request.password(), null);

    given(userRepository.save(any(User.class))).willReturn(saved);
    given(userStatusRepository.save(any(UserStatus.class))).willReturn(null);

    UserDto userDto = userService.create(request, Optional.empty());
    assertThat(userDto).isNotNull();
    assertThat(userDto.getEmail()).isEqualTo(saved.getEmail());
  }

  @Test
  @DisplayName("유저 업데이트 테스트")
  void updateTestSuccess() {

    UUID id = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("newName", "newEmail", "newPassword");
    User updated = new User(request.newUsername(), request.newEmail(), request.newPassword(), null);
    given(userRepository.findById(id)).willReturn(Optional.of(updated));

    given(userRepository.existsByEmail(request.newEmail())).willReturn(false);
    given(userRepository.existsByUsername(request.newUsername())).willReturn(false);

    UserDto userDto = userService.update(id, request, Optional.empty());

    verify(userRepository, times(1)).existsByEmail(request.newEmail());
    verify(userRepository, times(1)).existsByUsername(request.newUsername());
    verify(userRepository, times(1)).findById(id);

    assertThat(userDto).isNotNull();
    assertThat(userDto.getEmail()).isEqualTo(updated.getEmail());
  }

  @Test
  @DisplayName("유저 삭제 테스트")
  void deleteTestSuccess() {
    UUID id = UUID.randomUUID();

    User saved = new User("name", "test@test.com", "1234", null);
    given(userRepository.findById(id)).willReturn(Optional.of(saved));

    userService.delete(id);

    verify(userRepository, times(1)).findById(id);
    verify(userRepository, times(1)).deleteById(id);
    verify(userStatusRepository, times(1)).deleteByUserId(id);
  }
}
