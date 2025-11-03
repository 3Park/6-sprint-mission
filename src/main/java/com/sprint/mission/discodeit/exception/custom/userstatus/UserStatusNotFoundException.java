package com.sprint.mission.discodeit.exception.custom.userstatus;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;

public class UserStatusNotFoundException extends UserStatusException {

  public UserStatusNotFoundException(
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
