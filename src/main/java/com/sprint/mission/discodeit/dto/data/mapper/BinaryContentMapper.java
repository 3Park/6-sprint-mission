package com.sprint.mission.discodeit.dto.data.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BinaryContentMapper {

  BinaryContentMapper INSTANCE = Mappers.getMapper(BinaryContentMapper.class);

  BinaryContentDto toDto(BinaryContent content);
}
