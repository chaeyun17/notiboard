package notiboard.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "제목은 필수 입력입니다.")
    @Size(min = 1, max = 100, message = "제목은 100자 이하여야 합니다.")
    private String title;
    @NotNull(message = "내용은 필수 입력입니다.")
    private String content;
    @NotNull(message = "시작일시는 필수 입력입니다.")
    private LocalDateTime openingTime;
    @NotNull(message = "종료일시는 필수 입력입니다.")
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