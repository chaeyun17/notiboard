package notiboard.notice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import notiboard.member.dto.MemberDto;
import notiboard.notice.domain.Notice;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO for {@link Notice}
 */

public class NoticeDto implements Serializable {

  @AllArgsConstructor
  @Data
  @NoArgsConstructor
  public static class Request implements Serializable {

    @NotNull(message = "제목은 필수 입력입니다.")
    @Size(min = 1, max = 100, message = "제목은 100자 이하여야 합니다.")
    private String title;

    @NotNull(message = "내용은 필수 입력입니다.")
    private String content;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @NotNull(message = "시작일시는 필수 입력입니다.")
    private LocalDateTime openingTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @NotNull(message = "종료일시는 필수 입력입니다.")
    private LocalDateTime closingTime;

    @JsonInclude(Include.NON_NULL)
    private List<MultipartFile> attachments = new ArrayList<>();

    @JsonInclude(Include.NON_NULL)
    private List<Long> deleteAttachmentIds = new ArrayList<>();

    public void addAttachments(List<MultipartFile> files) {
      this.attachments.addAll(files);
    }

  }


  @AllArgsConstructor
  @Data
  @NoArgsConstructor
  public static class Response implements Serializable {

    private Long id;

    private String title;

    @JsonInclude(Include.NON_NULL)
    private String content;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime openingTime;

    @JsonInclude(Include.NON_NULL)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime closingTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @JsonInclude(Include.NON_NULL)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedAt;

    @JsonInclude(Include.NON_NULL)
    private List<AttachmentDto.Response> attachments;
    private PostStatsDto.Response postStats;
    private MemberDto.Response createdBy;

    public Response(Notice notice) {
      this.id = notice.getId();
      this.title = notice.getTitle().toText();
      this.content = notice.getContent().toText();
      this.openingTime = notice.getPostingPeriod().getOpeningTime();
      this.closingTime = notice.getPostingPeriod().getClosingTime();
      this.createdAt = notice.getCreatedAt();
      this.modifiedAt = notice.getModifiedAt();
      this.attachments = notice.getAttachments().stream()
          .map(AttachmentDto.Response::new).toList();
      this.postStats = new PostStatsDto.Response(notice.getPostStats());
      this.createdBy = new MemberDto.Response(notice.getCreatedBy());
    }

    public static Response ofViewCount(Notice notice, long viewCount) {
      Response response = new Response(notice);
      response.getPostStats().setViewCount(viewCount);
      return response;
    }

    public Response setViewCount(long viewCount) {
      this.postStats.setViewCount(viewCount);
      return this;
    }
  }
}