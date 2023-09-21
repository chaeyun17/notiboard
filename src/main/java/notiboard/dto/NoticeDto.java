package notiboard.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO for {@link notiboard.domain.Notice}
 */

public class NoticeDto implements Serializable {

  @AllArgsConstructor
  @Data
  @NoArgsConstructor
  public static class Request {

    private String title;
    private String content;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private List<MultipartFile> attachments = new ArrayList<>();

    public void addAttachments(List<MultipartFile> files) {
      this.attachments.addAll(files);
    }

  }


  @AllArgsConstructor
  @Data
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