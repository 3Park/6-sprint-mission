package com.sprint.mission.discodeit.exception.custom.userstatus;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;

public class UserStatusAlreadyExsistException extends UserStatusException {

  public UserStatusAlreadyExsistException(
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
