package com.sprint.mission.discodeit.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.ArrayList;
import java.util.List;

public class ChannelFixture {

  private static Channel createChannel(ChannelType channelType, String name, String description) {
    Channel channel = new Channel(channelType, name, description);
    return channel;
  }

  public static List<Channel> SetMockChannels(ChannelRepository channelRepository) {
    channelRepository.deleteAll();
    channelRepository.flush();

    List<Channel> channels = new ArrayList<>();
    channels.add(createChannel(ChannelType.PUBLIC, "channel1", "channel1"));
    channels.add(createChannel(ChannelType.PRIVATE, "", ""));

    channelRepository.save(channels.get(0));
    channelRepository.save(channels.get(1));

    return channels;
  }
}
