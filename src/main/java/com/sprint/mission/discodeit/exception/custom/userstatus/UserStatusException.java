package com.sprint.mission.discodeit.exception.custom.userstatus;

import com.sprint.mission.discodeit.exception.custom.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class UserStatusException extends DiscodeitException {

  public UserStatusException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
