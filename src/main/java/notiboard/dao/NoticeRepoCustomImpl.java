package notiboard.dao;

import static notiboard.domain.QNotice.notice;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import notiboard.domain.Notice;
import notiboard.dto.NoticeDto;
import notiboard.dto.NoticeDto.Response;
import notiboard.dto.SearchType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeRepoCustomImpl extends QuerydslRepositorySupport implements NoticeRepoCustom {

  public NoticeRepoCustomImpl() {
    super(Notice.class);
  }

  @Override
  public Page<Response> search(SearchType searchType, String keyword, LocalDateTime from,
      LocalDateTime to, Pageable pageable) {
    JPAQuery<Response> query = new JPAQuery<>(getEntityManager());
    QBean<Response> projection = Projections.fields(NoticeDto.Response.class,
        notice.id,
        notice.title.title,
        notice.postingPeriod.openingTime,
        notice.postingPeriod.closingTime,
        notice.createdAt,
        notice.modifiedAt);
    query = query.select(projection).from(notice);

    Predicate predicate = notice.deleted.isFalse();

    if (searchType.equals(SearchType.TITLE) && StringUtils.isNotBlank(keyword)) {
      predicate = notice.title.title.containsIgnoreCase(keyword);
    } else if (searchType.equals(SearchType.CONTENT) && StringUtils.isNotBlank(keyword)) {
      predicate = notice.content.content.contains(keyword);
    }
    if (from != null && to != null) {
      predicate = ExpressionUtils.and(predicate, notice.createdAt.between(from, to));
    }
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
        .where(notice.id.eq(id)).fetchOne();
    return Optional.ofNullable(result);
  }


}
