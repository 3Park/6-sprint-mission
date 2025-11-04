package com.sprint.mission.discodeit.message;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

  @InjectMocks
  private BasicMessageService messageService;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private UserRepository userRepository;

  @Test
  @DisplayName("메세지 생성 테스트")
  void createMessageTest() {
    UUID channelId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();

    Channel channel = new Channel(ChannelType.PUBLIC, "name", "description");
    User user = new User("name", "username", "password", null);

    MessageCreateRequest request = new MessageCreateRequest("comment", channelId, userId);
    Message message = new Message(request.content(), channel, user);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(messageRepository.save(any(Message.class))).willReturn(message);

    MessageDto dto = messageService.create(request, List.of());

    assertThat(dto.getContent()).isEqualTo(request.content());
    verify(channelRepository).findById(channelId);
    verify(userRepository).findById(userId);
    verify(messageRepository).save(any(Message.class));
  }

  @Test
  @DisplayName("메세지 업데이트 테스트")
  void updateMessageTest() {

    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("newContent");
    Channel channel = new Channel(ChannelType.PUBLIC, "name", "description");
    User user = new User("name", "username", "password", null);

    Message message = new Message(request.newContent(), channel, user);
    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

    MessageDto dto = messageService.update(messageId, request);
    assertThat(dto.getContent()).isEqualTo(request.newContent());
    verify(messageRepository).findById(messageId);
  }

  @Test
  @DisplayName("메세지 삭제 테스트")
  void deleteMessageTest() {
    UUID messageId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC, "name", "description");
    User user = new User("name", "username", "password", null);
    Message message = new Message("content", channel, user);

    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    messageService.delete(messageId);

    verify(messageRepository).findById(messageId);
    verify(messageRepository).deleteById(messageId);
  }

  @Test
  @DisplayName("채널아이디로 메세지 찾기 테스트")
  void findFyChannelIdMessageTest() {

    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC, "name", "description");
    User user = new User("name", "username", "password", null);
    Message message = new Message("content", channel, user);

    Slice<Message> returnVal = new Slice<Message>() {
      @Override
      public int getNumber() {
        return 1;
      }

      @Override
      public int getSize() {
        return 1;
      }

      @Override
      public int getNumberOfElements() {
        return 1;
      }

      @Override
      public List<Message> getContent() {
        return List.of(message);
      }

      @Override
      public boolean hasContent() {
        return true;
      }

      @Override
      public Sort getSort() {
        return Sort.by(Sort.Direction.DESC, "createdAt");
      }

      @Override
      public boolean isFirst() {
        return true;
      }

      @Override
      public boolean isLast() {
        return true;
      }

      @Override
      public boolean hasNext() {
        return false;
      }

      @Override
      public boolean hasPrevious() {
        return false;
      }

      @Override
      public Pageable nextPageable() {
        return null;
      }

      @Override
      public Pageable previousPageable() {
        return null;
      }

      @Override
      public <U> Slice<U> map(Function<? super Message, ? extends U> converter) {
        U converted = converter.apply(message);
        return new SliceImpl<>(
            List.of(converted),
            this.getPageable(),
            false // 다음 페이지 없음
        );
      }

      @Override
      public Iterator<Message> iterator() {
        return (Iterator<Message>) message;
      }
    };

    Pageable pageable = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "createdAt"));
    given(messageRepository.findAllByChannelId(channelId, pageable)).willReturn(returnVal);

    PageResponse<MessageDto> dto = messageService.findAllByChannelId(channelId, null, "desc");
    assertThat(dto.getContent().size()).isEqualTo(returnVal.getContent().size());
  }
}
