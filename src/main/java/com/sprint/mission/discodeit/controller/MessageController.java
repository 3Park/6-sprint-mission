package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.custom.message.MessageInputDataException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;

  @PostMapping
  public ResponseEntity<MessageDto> create(
      @RequestPart("messageCreateRequest") String req,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    try {
      List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
          .map(files -> files.stream()
              .map(file -> {
                try {
                  return new BinaryContentCreateRequest(
                      file.getOriginalFilename(),
                      file.getContentType(),
                      file.getBytes()
                  );
                } catch (IOException e) {
                  throw new MessageInputDataException(ErrorCode.INVALID_MESSAGE_DATA,
                      Map.of("attatchments", attachments));
                }
              })
              .toList())
          .orElse(null);

      MessageCreateRequest messageCreateRequest = new ObjectMapper().readValue(req,
          MessageCreateRequest.class);
      MessageDto createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(createdMessage);
    } catch (JsonProcessingException ex) {
      throw new MessageInputDataException(ErrorCode.INVALID_MESSAGE_DATA, Map.of("request", req));
    }
  }

  @PatchMapping(value = "/{messageId}")
  public ResponseEntity<MessageDto> update(@PathVariable("messageId") UUID messageId,
      @RequestBody @Valid MessageUpdateRequest request) {
    MessageDto updatedMessage = messageService.update(messageId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedMessage);
  }

  @DeleteMapping(value = "/{messageId}")
  public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId,
      @RequestParam(required = false) Instant createdAt,
      @RequestParam(defaultValue = "ASC") String orderBy) {
    PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, createdAt,
        orderBy);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }
}
