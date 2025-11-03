package com.sprint.mission.discodeit.exception.custom.channel;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
