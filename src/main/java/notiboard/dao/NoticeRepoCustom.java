package notiboard.dao;

import java.time.LocalDateTime;
import java.util.Optional;
import notiboard.domain.Notice;
import notiboard.dto.NoticeDto.Response;
import notiboard.dto.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepoCustom {

  Page<Response> search(SearchType searchType, String keyword, LocalDateTime from,
      LocalDateTime to, Pageable pageable);

  Optional<Notice> findByIdFetch(Long id);
}
