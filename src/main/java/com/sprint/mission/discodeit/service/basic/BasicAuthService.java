package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

//  @Transactional(readOnly = true)
//  @Override
//  public UserDto login(LoginRequest loginRequest) {
//    log.debug("로그인 시도: username={}", loginRequest.username());
//
//    String username = loginRequest.username();
//    String password = loginRequest.password();
//
//    User user = userRepository.findByUsername(username)
//        .orElseThrow(() -> UserNotFoundException.withUsername(username));
//
//    if (!user.getPassword().equals(password)) {
//      throw InvalidCredentialsException.wrongPassword();
//    }
//
//    log.info("로그인 성공: userId={}, username={}", user.getId(), username);
//    return userMapper.toDto(user);
//  }
}
