package com.sprint.mission.discodeit.exception.custom.message;

import com.sprint.mission.discodeit.exception.custom.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class MessageException extends DiscodeitException {

  public MessageException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
