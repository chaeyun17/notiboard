package notiboard.notice.dao;

import java.time.LocalDateTime;
import java.util.List;
import notiboard.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepoCustom {

  boolean existsByIdAndCreatedById(Long noticeId, Long memberId);

  List<Notice> findAllByPostingPeriodClosingTimeIsBefore(LocalDateTime now);
}
