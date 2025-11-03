package com.sprint.mission.discodeit.exception.custom.binary;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;

public class NoItemsException extends BinaryException {

  public NoItemsException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
