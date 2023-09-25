package notiboard.notice.dao;

import notiboard.notice.domain.PostStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostStatsRepository extends JpaRepository<PostStats, Long> {

  @Modifying
  @Query("UPDATE PostStats p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
  void increaseViewCount(@Param("id") Long id);
}
