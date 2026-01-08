package com.sprint.mission.discodeit.service.basic;

import com.nimbusds.jwt.JWTClaimsSet;
import com.sprint.mission.discodeit.dto.data.JwtDto;
import com.sprint.mission.discodeit.dto.data.JwtInformation;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.token.IllegalTokenException;
import com.sprint.mission.discodeit.jwt.JwtRegistry;
import com.sprint.mission.discodeit.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtRegistry jwtRegistry;

    @Override
    public JwtInformation refreshToken(String refreshToken) {

        try {
            JWTClaimsSet set = jwtTokenProvider.parseToken(refreshToken);
            if (set == null) {
                log.error("Invalid refresh token");
                throw new IllegalTokenException();
            }

            String newAccessToken = jwtTokenProvider.generateAccessToken(set.getSubject(), set.getClaimAsString("role"));
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(set.getSubject(), set.getClaimAsString("role"));

            User user = userRepository.findByUsername(set.getSubject())
                    .orElseThrow(() -> new UsernameNotFoundException(set.getSubject()));

            UserDto dto = userMapper.toDto(user);
            JwtDto jwtDto = JwtDto
                    .builder()
                    .userDto(dto)
                    .accessToken(newAccessToken)
                    .build();

            JwtInformation newInfo = JwtInformation.builder()
                    .dto(jwtDto)
                    .refreshToken(newRefreshToken)
                    .accessToken(newAccessToken)
                    .build();

            jwtRegistry.rotateJwtInformation(refreshToken, newInfo);
            return newInfo;

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
