package com.sprint.mission.discodeit.exception.custom.user;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;

public class InvalidPasswordException extends UserException {

  public InvalidPasswordException(
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
