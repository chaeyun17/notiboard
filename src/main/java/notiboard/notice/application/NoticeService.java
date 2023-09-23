package notiboard.notice.application;

import static notiboard.exception.ErrorCode.NOT_FOUND_NOTICE;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notiboard.common.applicaiton.PolicyChecker;
import notiboard.exception.CustomException;
import notiboard.exception.ErrorCode;
import notiboard.notice.dao.NoticeRepository;
import notiboard.notice.domain.Attachment;
import notiboard.notice.domain.Notice;
import notiboard.notice.dto.NoticeDto.Request;
import notiboard.notice.dto.NoticeDto.Response;
import notiboard.notice.dto.SearchType;
import notiboard.notice.dto.UploadFileDto;
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
  private final PolicyChecker policyChecker;

  @Transactional
  public Long create(Request request) {
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
  public Response findById(Long id) {
    Notice notice = findByIdFetchOrElseThrow(id);
    notice.increaseViewCnt();
    return new Response(notice);
  }

  @Transactional
  public void deleteById(Long id) {
    requiredSameAuthorWithLoggedIn(id);
    Notice notice = findByIdFetchOrElseThrow(id);
    attachmentService.deleteByNotice(notice);
    noticeRepository.deleteById(id);
  }

  public Page<Response> search(SearchType searchType, String keyword, LocalDateTime from,
      LocalDateTime to, Pageable pageable) {
    return noticeRepository.search(searchType, keyword, from, to, pageable);
  }

  @Transactional
  public Response modify(Long id, Request request) {
    requiredSameAuthorWithLoggedIn(id);
    Notice notice = findByIdFetchOrElseThrow(id);
    notice.modify(request);
    saveAttachments(notice, request.getAttachments());
    attachmentService.deleteAttachments(request.getDeleteAttachmentIds());
    return new Response(notice);
  }

  private Notice findByIdFetchOrElseThrow(Long id) {
    return noticeRepository.findByIdFetch(id)
        .orElseThrow(() -> new CustomException(NOT_FOUND_NOTICE));
  }


  private void requiredSameAuthorWithLoggedIn(Long noticeId) {
    findByIdFetchOrElseThrow(noticeId);
    if (noticeRepository.existsByIdAndCreatedById(noticeId, policyChecker.getLoggedUserId())) {
      return;
    }
    throw new CustomException(ErrorCode.FORBIDDEN_NOT_ALLOWED);
  }

}
