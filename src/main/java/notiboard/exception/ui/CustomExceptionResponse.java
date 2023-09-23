package notiboard.exception.ui;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import notiboard.exception.CustomException;
import org.springframework.http.HttpStatus;

@Data
public class CustomExceptionResponse {

  private LocalDateTime timestamp;
  private HttpStatus httpStatus;
  private String message;
  private Object description;

  @Builder
  public CustomExceptionResponse(HttpStatus httpStatus, String message, Object description) {
    this.timestamp = LocalDateTime.now();
    this.httpStatus = httpStatus;
    this.message = message;
    this.description = description;
  }

  public CustomExceptionResponse(CustomException exception) {
    this.timestamp = LocalDateTime.now();
    this.httpStatus = exception.getHttpStatus();
    this.message = exception.getMessage();
    this.description = exception.getDescription();
  }

  public CustomExceptionResponse(HttpStatus status, Exception exception) {
    this.timestamp = LocalDateTime.now();
    this.httpStatus = status;
    this.message = exception.getMessage();
    this.description = exception.getLocalizedMessage();
  }


}
