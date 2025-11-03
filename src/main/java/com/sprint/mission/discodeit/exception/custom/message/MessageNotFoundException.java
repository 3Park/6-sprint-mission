package com.sprint.mission.discodeit.exception.custom.message;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;

public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException(
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
