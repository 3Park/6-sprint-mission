package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.custom.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.custom.binary.EmptyIdsException;
import com.sprint.mission.discodeit.exception.custom.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.custom.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.custom.common.NoPathVariableException;
import com.sprint.mission.discodeit.exception.custom.message.MessageInputDataException;
import com.sprint.mission.discodeit.exception.custom.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.custom.readstatus.ReadStatusAlreadyExsistException;
import com.sprint.mission.discodeit.exception.custom.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.custom.user.UserAlreadyExsistsException;
import com.sprint.mission.discodeit.exception.custom.user.UserInputDataException;
import com.sprint.mission.discodeit.exception.custom.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.custom.user.UserProfileException;
import com.sprint.mission.discodeit.exception.custom.userstatus.UserStatusAlreadyExsistException;
import com.sprint.mission.discodeit.exception.custom.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import com.sprint.mission.discodeit.exception.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.NoSuchElementException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

  @ExceptionHandler(UserInputDataException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(UserInputDataException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(UserProfileException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(UserProfileException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(EmptyIdsException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(EmptyIdsException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(MessageInputDataException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(MessageInputDataException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(UserNotFoundException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.NOT_FOUND);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(ChannelNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(ChannelNotFoundException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.NOT_FOUND);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(MessageNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(MessageNotFoundException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.NOT_FOUND);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(ReadStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(
      ReadStatusNotFoundException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.NOT_FOUND);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(UserStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(
      UserStatusNotFoundException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.NOT_FOUND);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(PrivateChannelUpdateException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(
      PrivateChannelUpdateException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.NOT_ACCEPTABLE);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(UserAlreadyExsistsException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(
      UserAlreadyExsistsException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.CONFLICT);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(ReadStatusAlreadyExsistException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(
      ReadStatusAlreadyExsistException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.CONFLICT);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(UserStatusAlreadyExsistException.class)
  public ResponseEntity<ErrorResponse> handleUserInputDataException(
      UserStatusAlreadyExsistException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.CONFLICT);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatch(NoHandlerFoundException ex) {
    NoPathVariableException exception = new NoPathVariableException(ErrorCode.NO_PATH_VARIABLE,
        Map.of());
    ErrorResponse error = createErrorResponse(exception, HttpStatus.NOT_FOUND);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  /*
   * 모든 경우를 커스텀 Exception으로 만드는것은 맞지 않아
   * 일반 오류들도 ErrorRespose로 전달 할 수 있도록 추가.
   *  */

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      ConstraintViolationException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MissingServletRequestParameterException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  // 400 잘못된 요청
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  // 400 잘못된 타입 (예: UUID 형식 오류)
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  // 404 리소스를 찾을 수 없음
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElement(NoSuchElementException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.NOT_FOUND);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  // 405 지원하지 않는 메소드
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleMethodNotSupported(
      HttpRequestMethodNotSupportedException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.METHOD_NOT_ALLOWED);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  // 409 중복 등 충돌 오류
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
      DataIntegrityViolationException ex) {
    ErrorResponse error = createErrorResponse(ex, HttpStatus.CONFLICT);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  // 500 서버 내부 오류 (예상치 못한 모든 오류 처리)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    log.debug(ex.getMessage(), ex.getStackTrace());
    log.error(ex.getMessage(), ex);
    ErrorResponse error = createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  private ErrorResponse createErrorResponse(Exception ex, HttpStatus status) {

    Instant timeStamp = Instant.now();
    ErrorCode errorCode = ErrorCode.COMMON_EXCEPTION;
    Map<String, Object> errorDetails = new HashMap<>();

    try {

      if (ex instanceof DiscodeitException) {
        timeStamp = ((DiscodeitException) ex).getTimestamp();
        errorCode = ((DiscodeitException) ex).getErrorCode();
        errorDetails = ((DiscodeitException) ex).getDetails();
      } else if (ex instanceof MethodArgumentNotValidException) {
        errorDetails = ((MethodArgumentNotValidException) ex)
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(
                Collectors.toUnmodifiableMap(FieldError::getField, FieldError::getDefaultMessage
                    , (existing, replacement) -> existing + ", " + replacement));
      } else if (ex instanceof ConstraintViolationException) {
        errorDetails = ((ConstraintViolationException) ex)
            .getConstraintViolations()
            .stream()
            .collect(Collectors.toUnmodifiableMap(
                violation -> {
                  // 어떤 파라미터/속성에서 오류가 났는지
                  // ex: "createUser.name" → 마지막 점(.) 이후만 사용하면 "name"
                  String path = violation.getPropertyPath().toString();
                  int lastDot = path.lastIndexOf('.');
                  return lastDot != -1 ? path.substring(lastDot + 1) : path;
                },
                ConstraintViolation::getMessage,
                (existing, replacement) -> existing + ", " + replacement // 충돌 처리
            ));
      }

      ErrorResponse error = ErrorResponse.builder()
          .timestamp(timeStamp)
          .message(errorCode.getMessage())
          .code(errorCode.toString())
          .status(status.value())
          .exceptionType(ex.getClass().getName())
          .details(errorDetails).build();
      return error;
    } catch (Exception e) {
      log.error("ErrorResponse 생성 실패. exception 정보 : " + ex + " 오류 메세지 : ", e.getMessage());
      return null;
    }
  }

}
