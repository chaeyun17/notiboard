package notiboard.notice.dao;

import java.time.LocalDateTime;
import java.util.Optional;
import notiboard.notice.domain.Notice;
import notiboard.notice.dto.NoticeDto.Response;
import notiboard.notice.dto.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepoCustom {

  Page<Response> search(SearchType searchType, String keyword, LocalDateTime from,
      LocalDateTime to, Pageable pageable);

  Optional<Notice> findByIdFetch(Long id);
}
