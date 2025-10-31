package com.sprint.mission.discodeit.exception.custom.channel;

import com.sprint.mission.discodeit.exception.custom.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class ChannelException extends DiscodeitException {

  public ChannelException(
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
