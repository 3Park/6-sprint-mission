package com.sprint.mission.discodeit.exception.custom.common;

import com.sprint.mission.discodeit.exception.custom.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;
import lombok.Builder;

public class NoPathVariableException extends DiscodeitException {

  public NoPathVariableException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
