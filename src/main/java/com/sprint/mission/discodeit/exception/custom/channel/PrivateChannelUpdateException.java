package com.sprint.mission.discodeit.exception.custom.channel;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;

public class PrivateChannelUpdateException extends ChannelException {

  public PrivateChannelUpdateException(
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
