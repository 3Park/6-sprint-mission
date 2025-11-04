package com.sprint.mission.discodeit.message;

import static org.awaitility.Awaitility.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.exception.custom.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

  @MockitoBean
  private MessageService messageService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private JpaMetamodelMappingContext jpaMappingContext;

  @Test
  @DisplayName("메세지 생성 성공 테스트")
  void createMessageSuccessTest() throws Exception {
    UUID channelId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest("content", channelId,
        UUID.randomUUID());
    String json = objectMapper.writeValueAsString(request);

    MockMultipartFile file = new MockMultipartFile("messageCreateRequest", "",
        MediaType.TEXT_PLAIN_VALUE, json.getBytes());

    MessageDto dto = new MessageDto();
    dto.setContent(request.content());
    dto.setChannelId(channelId);

    when(messageService.create(any(MessageCreateRequest.class), any(List.class))).thenReturn(dto);

    mockMvc.perform(multipart("/api/messages")
            .file(file)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated());

  }

  @Test
  @DisplayName("채널아이디로 메세지 찾기 실패 테스트")
  void getAllMessagesByChannelIdFailureTest() throws Exception {
    UUID channelId = UUID.randomUUID();
    when(messageService.findAllByChannelId(any(UUID.class), any(),
        any(String.class))).thenThrow(new
        MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND, Map.of("channelId", channelId)));

    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}
