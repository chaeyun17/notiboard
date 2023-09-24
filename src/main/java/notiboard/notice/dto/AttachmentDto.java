package notiboard.notice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import notiboard.notice.domain.Attachment;


public class AttachmentDto implements Serializable {

  @Data
  @NoArgsConstructor
  public static class Response implements Serializable {

    private Long id;

    private String fileName;

    private String downloadUrl;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    public Response(Attachment attachment) {
      this.id = attachment.getId();
      this.fileName = attachment.getUploadFile().getFileName();
      this.downloadUrl = attachment.getDownloadUrl();
      this.createdAt = attachment.getCreatedAt();
    }

  }

}
