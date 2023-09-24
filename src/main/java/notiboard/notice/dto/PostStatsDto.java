package notiboard.notice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import notiboard.notice.domain.PostStats;

public class PostStatsDto implements Serializable {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(Include.NON_NULL)
  public static class Response implements Serializable {

    private Long id;
    private Long viewCount;

    public Response(PostStats postStats) {
      this.id = postStats.getId();
      this.viewCount = postStats.getViewCount();
    }
  }

}
