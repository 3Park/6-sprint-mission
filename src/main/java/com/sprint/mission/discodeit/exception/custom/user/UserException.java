package com.sprint.mission.discodeit.exception.custom.user;

import com.sprint.mission.discodeit.exception.custom.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;

public class UserException extends DiscodeitException {

  public UserException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
