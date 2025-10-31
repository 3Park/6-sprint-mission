package com.sprint.mission.discodeit.exception.custom.binary;

import com.sprint.mission.discodeit.exception.custom.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class BinaryException extends DiscodeitException {

  public BinaryException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
