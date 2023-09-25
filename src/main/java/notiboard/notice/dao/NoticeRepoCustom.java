package notiboard.notice.dao;

import java.util.Optional;
import notiboard.notice.domain.Notice;
import notiboard.notice.dto.NoticeDto.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepoCustom {

  Page<Response> search(Pageable pageable);

  Optional<Notice> findByIdFetch(Long id);
}
