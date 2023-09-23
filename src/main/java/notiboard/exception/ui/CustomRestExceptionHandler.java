package notiboard.exception.ui;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import notiboard.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomRestExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleAccessDeniedExceptions(AccessDeniedException ex) {
    HttpStatus forbidden = HttpStatus.FORBIDDEN;
    CustomExceptionResponse response = CustomExceptionResponse.builder()
        .description(ex.getMessage())
        .message("접근 권한이 없습니다. 인증정보와 권한을 확인해주세요.")
        .httpStatus(forbidden)
        .build();
    return ResponseEntity.status(forbidden).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    log.debug(ex.getMessage());
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    Map<String, String> errors = new HashMap<>();
    ex.getFieldErrors().forEach((fieldError) -> {
      String fieldName = fieldError.getField();
      String errorMessage = fieldError.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    CustomExceptionResponse response = CustomExceptionResponse.builder()
        .description(errors)
        .message("입력값을 확인해주세요.")
        .httpStatus(httpStatus)
        .build();
    return ResponseEntity.status(httpStatus).body(response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException ex) {
    log.debug(ex.getMessage());
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations().forEach((violation) -> {
      String fieldName = violation.getPropertyPath().toString();
      String errorMessage = violation.getMessage();
      errors.put(fieldName, errorMessage);
    });
    CustomExceptionResponse response = CustomExceptionResponse.builder()
        .description(errors)
        .message("입력값을 확인해주세요.")
        .httpStatus(httpStatus)
        .build();
    return ResponseEntity.status(httpStatus).body(response);
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<Object> customExceptionHandle(CustomException ex) {
    log.debug(ex.getMessage());
    CustomExceptionResponse response = new CustomExceptionResponse(ex);
    return ResponseEntity.status(ex.getHttpStatus()).body(response);
  }


  @ExceptionHandler(BindException.class)
  public ResponseEntity<Object> beanPropertyBindingExceptionHandle(BindException ex) {
    log.debug(ex.getMessage());
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    Map<String, String> errors = new HashMap<>();
    ex.getLocalizedMessage();
    ex.getFieldErrors().forEach((error) -> {
      String fieldName = error.getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    CustomExceptionResponse response = CustomExceptionResponse.builder()
        .description(errors)
        .message("입력값을 확인해주세요.")
        .httpStatus(httpStatus)
        .build();
    return ResponseEntity.status(httpStatus).body(response);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<Object> noSuchElementExceptionHandle(NoSuchElementException ex) {
    log.debug(ex.getMessage());
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    CustomExceptionResponse response = CustomExceptionResponse.builder()
        .description(ex.getMessage())
        .message("해당 데이터를 찾지 못했습니다.")
        .httpStatus(httpStatus)
        .build();
    return ResponseEntity.status(httpStatus).body(response);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> global(RuntimeException ex) {
    log.debug(ex.getMessage());
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    CustomExceptionResponse response = new CustomExceptionResponse(httpStatus, ex);
    return ResponseEntity.status(httpStatus).body(response);
  }

}