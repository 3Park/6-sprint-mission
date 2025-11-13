package com.sprint.mission.discodeit.exception.custom.binary;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;

public class BinaryNotFoundException extends BinaryException {

  public BinaryNotFoundException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
