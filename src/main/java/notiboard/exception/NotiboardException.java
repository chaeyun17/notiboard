package notiboard.exception;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NotiboardException extends RuntimeException {

  private HttpStatus httpStatus;
  private String message;
  private String description;

  public NotiboardException(HttpStatus httpStatus, String message, String descriptionPattern,
      Object... descArgs) {
    this.httpStatus = httpStatus;
    this.message = message;
    this.description = ParameterizedMessage.format(descriptionPattern, descArgs);
  }

  public NotiboardException(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
    this.description = "No Description";
  }

  public NotiboardException(ErrorCode errorCode) {
    this.httpStatus = errorCode.getStatusCode();
    this.message = errorCode.getMessage();
    this.description = "No Description";
  }

}
