package com.sprint.mission.discodeit.exception.custom.readstatus;

import com.sprint.mission.discodeit.exception.custom.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class ReadStatusException extends DiscodeitException {

  public ReadStatusException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
