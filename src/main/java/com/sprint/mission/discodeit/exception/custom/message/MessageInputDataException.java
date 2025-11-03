package com.sprint.mission.discodeit.exception.custom.message;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;

public class MessageInputDataException extends MessageException {

  public MessageInputDataException(
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
