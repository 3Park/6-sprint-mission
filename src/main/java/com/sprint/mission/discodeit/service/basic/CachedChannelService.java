package com.sprint.mission.discodeit.service.basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CachedChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelMapper channelMapper;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    @Cacheable(value = "channels", key = "#userId", unless = "#result.length() == 0")
    public String findAllByUserId(UUID userId) {
        try {
            List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
                    .map(ReadStatus::getChannel)
                    .map(Channel::getId)
                    .toList();

            List<ChannelDto> dto = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
                    .stream()
                    .map(channelMapper::toDto)
                    .toList();

            if (dto.isEmpty())
                return "";

            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new ChannelNotFoundException();
        }
    }
}
