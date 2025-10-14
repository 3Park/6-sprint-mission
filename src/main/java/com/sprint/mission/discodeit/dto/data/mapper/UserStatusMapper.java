package com.sprint.mission.discodeit.dto.data.mapper;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserStatusMapper {

  UserStatusMapper INSTANCE = Mappers.getMapper(UserStatusMapper.class);

  @Mapping(source = "user.id", target = "userId")
  UserStatusDto toDto(UserStatus userStatus);
}
