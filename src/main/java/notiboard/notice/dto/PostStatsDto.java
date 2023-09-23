package notiboard.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import notiboard.notice.domain.PostStats;

public class PostStatsDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    private Long viewCount;

    public Response(PostStats postStats) {
      this.viewCount = postStats.getViewCount();
    }
  }

}
