package notiboard.notice.dao;

import java.util.List;
import notiboard.notice.domain.Attachment;
import notiboard.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

  void deleteAllByNotice(Notice notice);

  List<Attachment> findAllByNotice(Notice notice);
}
