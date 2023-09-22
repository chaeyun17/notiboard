package notiboard.notice.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import notiboard.notice.domain.Attachment;


public class AttachmentDto {

  @Data
  @NoArgsConstructor
  public static class Response {

    private Long id;
    private String fileName;
    private String downloadUrl;
    private LocalDateTime createdAt;

    public Response(Attachment attachment) {
      this.id = attachment.getId();
      this.fileName = attachment.getUploadFile().getFileName();
      this.downloadUrl = attachment.getDownloadUrl();
      this.createdAt = attachment.getCreatedAt();
    }

  }

}
