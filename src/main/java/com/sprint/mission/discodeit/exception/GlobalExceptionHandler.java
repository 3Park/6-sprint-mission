package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.custom.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.custom.user.UserInputDataException;
import com.sprint.mission.discodeit.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

  @ExceptionHandler(UserInputDataException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(UserInputDataException ex) {
    ErrorResponse error = createErrorResponse(ex, "UC001", HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  private ErrorResponse createErrorResponse(DiscodeitException ex, String code, HttpStatus status) {
    try {
      ErrorResponse error = ErrorResponse.builder()
          .timestamp(ex.getTimestamp())
          .message(ex.getErrorCode().getMessage())
          .code(code)
          .status(status.value())
          .exceptionType(ex.getClass().getName())
          .details(ex.getDetails()).build();
      return error;
    } catch (Exception e) {
      log.error("ErrorResponse 생성 실패. exception 정보 : " + ex + " 오류 메세지 : ", e.getMessage());
      return null;
    }
  }

  //각 커스텀 exception 으로 변경필요
//  @ExceptionHandler(IllegalArgumentException.class)
//  public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e) {
//    return ResponseEntity
//        .status(HttpStatus.BAD_REQUEST)
//        .body(e.getMessage());
//  }
//
//  //각 커스텀 exception 으로 변경필요
//  @ExceptionHandler(NoSuchElementException.class)
//  public ResponseEntity<ErrorResponse> handleException(NoSuchElementException e) {
//    return ResponseEntity
//        .status(HttpStatus.NOT_FOUND)
//        .body(e.getMessage());
//  }
//
//  @ExceptionHandler(MethodArgumentNotValidException.class)
//  public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
//    return ResponseEntity
//        .status(HttpStatus.NOT_FOUND)
//        .body(e.getMessage());
//  }
//
//  //각 커스텀 exception 으로 변경필요
//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<ErrorResponse> handleException(Exception e) {
//    return ResponseEntity
//        .status(HttpStatus.INTERNAL_SERVER_ERROR)
//        .body(e.getMessage());
//  }
}
