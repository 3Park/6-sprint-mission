package com.sprint.mission.discodeit.exception.custom.binary;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.util.Map;

public class BinaryS3Exception extends BinaryException {

  public BinaryS3Exception(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
