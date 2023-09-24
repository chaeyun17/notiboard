package notiboard.notice.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import notiboard.notice.domain.PostStats;

public class PostStatsDto implements Serializable {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response implements Serializable {

    private Long viewCount;

    public Response(PostStats postStats) {
      this.viewCount = postStats.getViewCount();
    }
  }

}
