package com.sprint.mission.discodeit.dto.data.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {

  MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

  @Mapping(source = "channel.id", target = "channelId")
  MessageDto toDto(Message message);
}
