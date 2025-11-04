package com.sprint.mission.discodeit.channel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.custom.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ChannelController.class)
public class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ChannelService channelService;

  @MockitoBean
  private JpaMetamodelMappingContext jpaMappingContext;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("public 채널 생성 성공 테스트")
  void createChannelSuccessTest() throws Exception {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("name", "description");
    ChannelDto channelDto = new ChannelDto();
    channelDto.setName(request.name());
    channelDto.setDescription(request.description());
    channelDto.setType(ChannelType.PUBLIC.toString());

    when(channelService.create(any(PublicChannelCreateRequest.class))).thenReturn(channelDto);

    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value(request.name()))
        .andExpect(jsonPath("$.description").value(request.description()))
        .andExpect(jsonPath("$.type").value(ChannelType.PUBLIC.toString()));
  }

  @Test
  @DisplayName("findById 실패 테스트")
  void findByIdFailureTest() throws Exception {
    UUID userId = UUID.randomUUID();

    when(channelService.findAllByUserId(userId)).thenThrow(new ChannelNotFoundException(
        ErrorCode.CHANNEL_NOT_FOUND, Map.of("userId", userId)));

    mockMvc.perform(get("/api/channels")
            .param("userId", String.valueOf(userId))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("userId", userId))))
        .andExpect(status().isNotFound());
  }
}
