package com.sprint.mission.discodeit.user;

import static org.awaitility.Awaitility.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserStatusService userStatusService;

  @MockitoBean
  private JpaMetamodelMappingContext jpaMappingContext;

  @Test
  @DisplayName("유저 생성 성공")
  void createUserSuccessTest() throws Exception {
    UserCreateRequest request = new UserCreateRequest("test", "test@test.com", "1234");
    String json = objectMapper.writeValueAsString(request);

    UserDto userDto = new UserDto();
    userDto.setUsername(request.username());
    userDto.setEmail(request.email());
    userDto.setId(UUID.randomUUID());

    MockMultipartFile file = new MockMultipartFile("userCreateRequest", "",
        MediaType.TEXT_PLAIN_VALUE, json.getBytes());

    when(userService.create(any(UserCreateRequest.class), any(Optional.class))).thenReturn(userDto);

    mockMvc.perform(multipart("/api/users")
            .file(file)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value(userDto.getUsername()));
  }

  @Test
  @DisplayName("유저 생성 실패 테스트")
  void createUserFailureTest() throws Exception {

    String json = "{username";

    MockMultipartFile file = new MockMultipartFile("userCreateRequest", "",
        MediaType.TEXT_PLAIN_VALUE, json.getBytes());

    mockMvc.perform(multipart("/api/users")
            .file(file)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().is4xxClientError());
  }

}
