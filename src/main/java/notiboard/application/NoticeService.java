package notiboard.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notiboard.dao.NoticeRepository;
import notiboard.domain.Attachment;
import notiboard.domain.Notice;
import notiboard.dto.NoticeDto;
import notiboard.dto.UploadFileDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

  private final NoticeRepository noticeRepository;
  private final AttachmentService attachmentService;

  @Transactional
  public Long create(NoticeDto.Request request) {
    Notice notice = noticeRepository.save(Notice.of(request));
    List<UploadFileDto> uploadFileDtoList = request.getAttachments().stream()
        .map(UploadFileDto::new)
        .toList();
    List<Attachment> attachments = attachmentService.saveFiles(uploadFileDtoList, notice);
    notice.addAttachments(attachments);
    return notice.getId();
  }
}
