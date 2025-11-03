package com.sprint.mission.discodeit.exception.custom.binary;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;

public class EmptyIdsException extends BinaryException {

  public EmptyIdsException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
