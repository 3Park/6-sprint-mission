package com.sprint.mission.discodeit.exception.token;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class IllegalTokenException extends DiscodeitException {
    public IllegalTokenException() {
        super(ErrorCode.ILLEGAL_TOKEN_ERROR);
    }
}
