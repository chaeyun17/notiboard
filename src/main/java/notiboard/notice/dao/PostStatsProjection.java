package notiboard.notice.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import notiboard.notice.domain.QPostStats;
import notiboard.notice.dto.PostStatsDto;

public class PostStatsProjection {

  public static QBean<PostStatsDto.Response> projection(QPostStats postStats) {
    return Projections.fields(PostStatsDto.Response.class,
        postStats.viewCount);
  }

}
