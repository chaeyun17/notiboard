package notiboard.application;

import static notiboard.error.ErrorCode.NOT_FOUND_NOTICE;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notiboard.dao.NoticeRepository;
import notiboard.domain.Attachment;
import notiboard.domain.Notice;
import notiboard.dto.NoticeDto;
import notiboard.dto.NoticeDto.Request;
import notiboard.dto.NoticeDto.Response;
import notiboard.dto.SearchType;
import notiboard.dto.UploadFileDto;
import notiboard.error.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    saveAttachments(notice, request.getAttachments());
    return notice.getId();
  }

  private List<Attachment> saveAttachments(Notice notice, List<MultipartFile> uploadFiles) {
    List<UploadFileDto> uploadFileDtoList = uploadFiles.stream()
        .map(UploadFileDto::new)
        .toList();
    return attachmentService.saveFiles(uploadFileDtoList, notice);
  }

  @Transactional
  public NoticeDto.Response findById(Long id) {
    Notice notice = findByIdFetchOrElseThrow(id);
    notice.increaseViewCnt();
    return new NoticeDto.Response(notice);
  }

  @Transactional
  public void deleteById(Long id) {
    Notice notice = findByIdFetchOrElseThrow(id);
    attachmentService.deleteByNotice(notice);
    noticeRepository.deleteById(id);
  }

  public Page<NoticeDto.Response> search(SearchType searchType, String keyword, LocalDateTime from,
      LocalDateTime to, Pageable pageable) {
    return noticeRepository.search(searchType, keyword, from, to, pageable);
  }

  @Transactional
  public Response modify(Long id, Request request) {
    Notice notice = findByIdFetchOrElseThrow(id);
    notice.modify(request);
    saveAttachments(notice, request.getAttachments());
    attachmentService.deleteAttachments(request.getDeleteAttachmentIds());
    return new NoticeDto.Response(notice);
  }

  private Notice findByIdFetchOrElseThrow(Long id) {
    return noticeRepository.findByIdFetch(id)
        .orElseThrow(() -> new CustomException(NOT_FOUND_NOTICE));
  }

}
