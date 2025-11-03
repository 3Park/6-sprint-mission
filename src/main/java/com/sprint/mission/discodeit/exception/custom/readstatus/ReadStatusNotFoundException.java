package com.sprint.mission.discodeit.exception.custom.readstatus;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
