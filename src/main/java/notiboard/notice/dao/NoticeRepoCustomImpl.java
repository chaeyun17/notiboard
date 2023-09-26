package notiboard.notice.dao;

import static notiboard.notice.domain.QNotice.notice;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import notiboard.member.dao.MemberProjection;
import notiboard.notice.domain.Notice;
import notiboard.notice.dto.NoticeDto.Response;
import notiboard.notice.util.TimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeRepoCustomImpl extends QuerydslRepositorySupport implements NoticeRepoCustom {

  private final TimeUtils timeUtils;

  public NoticeRepoCustomImpl(TimeUtils timeUtils) {
    super(Notice.class);
    this.timeUtils = timeUtils;
  }

  @Override
  public Page<Response> search(Pageable pageable) {
    JPAQuery<Response> query = new JPAQuery<>(getEntityManager());
    QBean<Response> projection = Projections.fields(Response.class,
        notice.id,
        notice.title.title,
        notice.content.content,
        notice.postingPeriod.openingTime,
        notice.createdAt,
        PostStatsProjection.projection(notice.postStats).as("postStats"),
        MemberProjection.projection(notice.createdBy).as("createdBy"));
    query = query.select(projection).from(notice);

    LocalDateTime now = timeUtils.now();
    Predicate predicate = ExpressionUtils.allOf(
        ExpressionUtils.anyOf(
            notice.postingPeriod.openingTime.before(now),
            notice.postingPeriod.openingTime.eq(now)),
        ExpressionUtils.anyOf(
            notice.postingPeriod.closingTime.after(now),
            notice.postingPeriod.closingTime.eq(now))
    );

    query.where(predicate);

    List<Response> notices = Objects.requireNonNull(getQuerydsl())
        .applyPagination(pageable, query)
        .fetch();
    return new PageImpl<>(notices, pageable, query.fetch().size());
  }

  @Override
  public Optional<Notice> findByIdFetch(Long id) {
    JPAQuery<Notice> query = new JPAQuery<>(getEntityManager());
    Notice result = query.from(notice)
        .innerJoin(notice.attachments)
        .fetchJoin()
        .innerJoin(notice.postStats)
        .fetchJoin()
        .innerJoin(notice.createdBy)
        .fetchJoin()
        .where(notice.id.eq(id)).fetchOne();
    return Optional.ofNullable(result);
  }


}
