package com.sprint.mission.discodeit.exception.errorcode;

import lombok.Getter;

@Getter
public enum ErrorCode {
  INVALID_USER_DATA("입력한 사용자 정보가 잘못 되었습니다"),
  INVALID_PROFILE_DATA("프로필 데이터가 잘못 되었습니다"),
  INVALID_MESSAGE_DATA("입력한 메세지 데이터가 잘못 되었습니다."),
  INVALID_USER_PASSWORD("비밀번호가 맞지 않습니다."),
  INVALID_BINARY_DATA("파일이 없습니다."),
  NO_PATH_VARIABLE("Path 값이 없습니다"),
  NO_ID_VARIABLE("ID 값이 없습니다."),
  USER_NOT_FOUND("사용자를 찾을 수 없습니다"),
  DUPLICATE_USER("이미 있는 사용자 입니다"),
  DUPLICATE_READSTATUS("이미 있는 정보 입니다."),
  DUPLICATE_USERSTATUS("이미 있는 정보 입니다."),
  READSTATUS_NOT_FOUND("정보를 찾을 수 없습니다."),
  USERSTATUS_NOT_FOUND("정보를 찾을 수 없습니다."),
  CHANNEL_NOT_FOUND("없는 채널 입니다."),
  MESSAGE_NOT_FOUND("없는 메세지 입니다."),
  PRIVATE_CHANNEL_UPDATE("개인채널이 업데이트 되었습니다"),
  COMMON_EXCEPTION("오류가 발생 하였습니다.");

  private String message;

  ErrorCode(String message) {
    this.message = message;
  }

}
