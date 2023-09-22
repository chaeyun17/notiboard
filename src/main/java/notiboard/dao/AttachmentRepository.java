package notiboard.dao;

import java.util.List;
import notiboard.domain.Attachment;
import notiboard.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

  void deleteAllByNotice(Notice notice);

  List<Attachment> findAllByNotice(Notice notice);
}
