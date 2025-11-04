package com.sprint.mission.discodeit.message;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

public class MessageFixture {

  public static Message createMessage(String content, Channel channel, User user) {
    Message message = new Message(content, channel, user);
    return message;
  }
}
