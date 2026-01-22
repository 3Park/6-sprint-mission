package com.sprint.mission.discodeit.service.basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CachedUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    @Cacheable(
            value = "users",
            key = "'all'",
            unless = "#result.length() == 0"
    )
    @Transactional(readOnly = true)
    public String findAll() {
        log.debug("모든 사용자 조회 시작");
        List<UserDto> userDtos = userRepository.findAllWithProfileAndStatus()
                .stream()
                .map(userMapper::toDto)
                .toList();
        log.info("모든 사용자 조회 완료: 총 {}명", userDtos.size());

        if (userDtos.isEmpty()) {
            return "";
        }

        try {
            return objectMapper.writeValueAsString(userDtos);
        } catch (JsonProcessingException e) {
            throw new UserException(ErrorCode.INVALID_REQUEST);
        }
    }
}
