package com.sprint.mission.discodeit.exception.custom.base;

import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
public class DiscodeitException extends RuntimeException {

  private final Instant timestamp;
  private final ErrorCode errorCode;
  private final Map<String, Object> details;

  @Override
  public String toString() {
    return "DiscodeitException{" +
        "timestamp = " + timestamp +
        ", errorCode = " + errorCode +
        ", details=" + details +
        '}';
  }

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.details = details;
  }
}
