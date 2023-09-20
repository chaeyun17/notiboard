package notiboard.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for {@link notiboard.domain.Notice}
 */

public class NoticeDto implements Serializable {


  @AllArgsConstructor
  @Getter
  @ToString
  @NoArgsConstructor
  public static class Response {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
  }
}