package com.sprint.mission.discodeit.exception.custom.readstatus;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;

public class ReadStatusAlreadyExsistException extends ReadStatusException {

  public ReadStatusAlreadyExsistException(
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
