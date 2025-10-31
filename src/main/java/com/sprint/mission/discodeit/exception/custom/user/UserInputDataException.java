package com.sprint.mission.discodeit.exception.custom.user;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class UserInputDataException extends UserException {

  public UserInputDataException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
