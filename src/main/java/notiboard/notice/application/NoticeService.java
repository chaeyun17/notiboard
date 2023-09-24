package notiboard.notice.application;

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
import notiboard.notice.dto.NoticeDto;
import notiboard.notice.dto.NoticeDto.Request;
import notiboard.notice.dto.NoticeDto.Response;
import notiboard.notice.dto.PageDto;
import notiboard.notice.dto.SearchType;
import notiboard.notice.dto.UploadFileDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

  public static final String CACHE_NOTICES = "notices";
  public static final String CACHE_NOTICE = "notice";
  public static final String NOTICE_CACHE_MANAGER = "noticeCacheManager";
  private final NoticeRepository noticeRepository;
  private final AttachmentService attachmentService;
  private final PolicyChecker policyChecker;
  private final NoticeContentService noticeContentService;
  private final PostStatsService postStatsService;


  @Transactional
  @CacheEvict(value = CACHE_NOTICES, allEntries = true, cacheManager = NOTICE_CACHE_MANAGER)
  public Long create(Request request) {
    Notice notice = noticeRepository.save(Notice.of(request));
    saveAttachments(notice, request.getAttachments());
    return notice.getId();
  }

  public Response findById(Long id) {
    NoticeDto.Response notice = noticeContentService.findById(id);
    long viewCount = postStatsService.increaseViewCnt(notice.getPostStats().getId());
    return notice.setViewCount(viewCount);
  }

  @Caching(evict = {
      @CacheEvict(value = CACHE_NOTICE, key = "#id", cacheManager = NOTICE_CACHE_MANAGER),
      @CacheEvict(value = CACHE_NOTICES, allEntries = true, cacheManager = NOTICE_CACHE_MANAGER)
  })
  @Transactional
  public void deleteById(Long id) {
    requiredSameAuthorWithLoggedIn(id);
    Notice notice = findByIdFetchOrElseThrow(id);
    attachmentService.deleteByNotice(notice);
    noticeRepository.deleteById(id);
  }

  @Cacheable(value = CACHE_NOTICES, cacheManager = NOTICE_CACHE_MANAGER)
  public PageDto<Response> search(SearchType searchType, String keyword, LocalDateTime from,
      LocalDateTime to, Pageable pageable) {
    Page<Response> notices = noticeRepository.search(searchType, keyword, from, to, pageable);
    return new PageDto<>(notices);
  }

  @Caching(evict = {
      @CacheEvict(value = CACHE_NOTICE, key = "#id", cacheManager = NOTICE_CACHE_MANAGER),
      @CacheEvict(value = CACHE_NOTICES, allEntries = true, cacheManager = NOTICE_CACHE_MANAGER)
  })
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
    return noticeContentService.findByIdFetchOrElseThrow(id);
  }

  private void requiredSameAuthorWithLoggedIn(Long noticeId) {
    findByIdFetchOrElseThrow(noticeId);
    if (noticeRepository.existsByIdAndCreatedById(noticeId, policyChecker.getLoggedUserId())) {
      return;
    }
    throw new CustomException(ErrorCode.FORBIDDEN_NOT_ALLOWED);
  }

  private List<Attachment> saveAttachments(Notice notice, List<MultipartFile> uploadFiles) {
    List<UploadFileDto> uploadFileDtoList = uploadFiles.stream()
        .map(UploadFileDto::new)
        .toList();
    return attachmentService.saveFiles(uploadFileDtoList, notice);
  }
}
