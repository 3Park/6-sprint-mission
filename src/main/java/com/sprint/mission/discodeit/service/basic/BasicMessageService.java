package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.mapper.MessageMapper;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.custom.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.custom.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.custom.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageAttatchmentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  //
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final MessageAttatchmentRepository messageAttatchmentRepository;

  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();
    Channel channel = channelRepository.findById(channelId).orElseThrow(
        () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
            Map.of("channdId", channelId)));
    User user = userRepository.findById(authorId).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("authorId", authorId)));

    String content = messageCreateRequest.content();
    Message message = new Message(
        content,
        channel,
        user
    );

    log.info(
        String.format("Creating message with content: %s, channelId : %s, userId : %s", content,
            channelId, user.getId()));

    List<MessageAttachment> attachmentIds = Optional.ofNullable(binaryContentCreateRequests)
        .map(requests -> requests.stream()
            .map(attachmentRequest -> {
              String fileName = attachmentRequest.fileName();
              String contentType = attachmentRequest.contentType();
              byte[] bytes = attachmentRequest.bytes();

              BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                  contentType);
              BinaryContent created = binaryContentRepository.save(binaryContent);
              binaryContentStorage.put(created.getId(), bytes);

              return new MessageAttachment(message, created);
            })
            .toList()
        )
        .orElse(List.of());

    message.setAttatchments(attachmentIds);
    Message createdMessage = messageRepository.save(message);
    MessageDto messageDto = MessageMapper.INSTANCE.toDto(createdMessage);
    messageDto.setAttachments(createdMessage.getAttachments());
    return messageDto;
  }

  @Override
  @Transactional(readOnly = true)
  public MessageDto find(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND,
                Map.of("messageId", messageId)));

    MessageDto messageDto = MessageMapper.INSTANCE.toDto(message);
    messageDto.setAttachments(message.getAttachments());
    return messageDto;
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createdAt,
      String pageSortType) {

    Sort.Direction direction = Sort.Direction.ASC;
    if (pageSortType.equalsIgnoreCase("desc")) {
      direction = Sort.Direction.DESC;
    }

    Pageable pageable = PageRequest.of(0, 50, Sort.by(direction, "createdAt"));

    if (createdAt != null) {
      return PageResponseMapper.fromSlice(
          messageRepository.findSliceAllByChannelIdAndCreatedAtAfter(channelId, createdAt, pageable)
              .map(x -> {
                MessageDto dto = MessageMapper.INSTANCE.toDto(x);
                dto.setAttachments(x.getAttachments());
                return dto;
              }));
    } else {
      return PageResponseMapper.fromSlice(
          messageRepository.findAllByChannelId(channelId, pageable)
              .map(x -> {
                MessageDto dto = MessageMapper.INSTANCE.toDto(x);
                dto.setAttachments(x.getAttachments());
                return dto;
              }));
    }
  }

  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND,
                Map.of("messageId", messageId)));
    message.update(newContent);

    MessageDto messageDto = MessageMapper.INSTANCE.toDto(message);
    messageDto.setAttachments(message.getAttachments());

    return messageDto;
  }

  @Override
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND,
                Map.of("messageId", messageId)));

    Optional.ofNullable(message.getAttachments())
        .ifPresent(attachments -> attachments.forEach(x -> {
          binaryContentRepository.deleteById(x.getId());
          binaryContentStorage.delete(x.getId());
        }));

    messageRepository.deleteById(messageId);
  }
}
