package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TotalTest {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private UserService userService;

  @Autowired
  private UserController userController;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ChannelService channelService;

  @Autowired
  private ChannelController channelController;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  @DisplayName("사용자 생성 테스트")
  @Transactional
  void createUserTest() throws JsonProcessingException {
    UserCreateRequest request = new UserCreateRequest("test", "test@test.com", "1234");
    String json = objectMapper.writeValueAsString(request);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    HttpHeaders textHeaders = new HttpHeaders();
    textHeaders.setContentType(MediaType.TEXT_PLAIN);
    HttpEntity<String> textPart = new HttpEntity<>(json, textHeaders);
    body.add("userCreateRequest", textPart);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

    ResponseEntity<UserDto> response = restTemplate.postForEntity("/api/users", entity,
        UserDto.class);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getUsername()).isEqualTo("test");
  }

  @Test
  @DisplayName("채널 생성 테스트")
  void channelCreateTest() throws JsonProcessingException {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("name", "description");

    ResponseEntity<ChannelDto> response = restTemplate.postForEntity("/api/channels/public",
        request,
        ChannelDto.class);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getName()).isEqualTo("name");
  }
}
