package notiboard.notice.dao;

import notiboard.notice.domain.PostStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostStatsRepository extends JpaRepository<PostStats, Long> {

}
