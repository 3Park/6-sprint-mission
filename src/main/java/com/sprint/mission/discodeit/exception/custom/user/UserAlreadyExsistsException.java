package com.sprint.mission.discodeit.exception.custom.user;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class UserAlreadyExsistsException extends UserException {

  public UserAlreadyExsistsException(
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
