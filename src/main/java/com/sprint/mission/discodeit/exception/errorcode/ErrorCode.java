package com.sprint.mission.discodeit.exception.errorcode;

import com.sprint.mission.discodeit.exception.response.ErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum ErrorCode {
  INVALID_UER_CREATE_DATA("사용자 정보가 잘못되었습니다"),
  USER_NOT_FOUND("사용자를 찾을 수 없습니다"),
  DUPLICATE_USER("이미 있는 사용자 입니다"),
  CHANNEL_NOT_FOUND("없는 채널 입니다."),
  PRIVATE_CHANNEL_UPDATE("개인채널이 업데이트 되었습니다");

  private String message;

  ErrorCode(String message) {
    this.message = message;
  }

}
